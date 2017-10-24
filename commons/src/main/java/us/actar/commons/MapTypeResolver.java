package us.actar.commons;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.util.Types;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static us.actar.commons.Injector.getMap;

public class MapTypeResolver<TYPE> implements TypeIdResolver {

  private JavaType baseType;

  private final TypeLiteral<Class<? extends TYPE>> classType;

  private Map<String, Class<? extends TYPE>> mapping;

  private Map<Class<? extends TYPE>, String> reverseMapping;

  public MapTypeResolver (TypeLiteral<TYPE> type) {
    this.classType = newClassTypeLiteral (type);
  }

  public interface RegistryBuilder<TYPE> {

    RegistryBuilder<TYPE> registerType (String typeId, Class<? extends TYPE> typeClass);

    void done ();

  }

  public static <TYPE> RegistryBuilder<TYPE> newBuilder (Binder binder, TypeLiteral<TYPE> baseType) {
    Map<String, Class<? extends TYPE>> map = new HashMap<> ();
    return new RegistryBuilder<TYPE> () {
      @Override
      public RegistryBuilder<TYPE> registerType (String typeId, Class<? extends TYPE> typeClass) {
        map.put (typeId, typeClass);
        return this;
      }

      @Override
      public void done () {
        if (map.size () == 0)
          return;

        MapBinder<String, Class<? extends TYPE>> mb =
          newMapBinder (binder, TypeLiteral.get (String.class), newClassTypeLiteral (baseType));

        map.forEach ((k, v) -> mb.addBinding (k).toInstance (v));
      }
    };
  }

  private static <TYPE> TypeLiteral<Class<? extends TYPE>> newClassTypeLiteral (TypeLiteral<TYPE> type) {
    return (TypeLiteral<Class<? extends TYPE>>) TypeLiteral.get (
      Types.newParameterizedType (Class.class, Types.subtypeOf (type.getType ())));
  }

  @Override
  public JsonTypeInfo.Id getMechanism () {
    return JsonTypeInfo.Id.CUSTOM;
  }

  @Override
  public void init (JavaType baseType) {
    this.baseType = baseType;
    this.mapping = Injector.getMap (TypeLiteral.get (String.class), classType);
    this.reverseMapping = this.mapping.entrySet ().stream ().collect (toMap (Entry::getValue, Entry::getKey));
  }

  @Override
  public String idFromValue (Object value) {
    return idFromValueAndType (value, value.getClass ());
  }

  @Override
  public String idFromBaseType () {
    return idFromValueAndType (null, baseType.getRawClass ());
  }

  @Override
  public String idFromValueAndType (Object obj, Class<?> typeClass) {
    return ofNullable (reverseMapping.get (typeClass))
      .orElseThrow (() -> new IllegalStateException ("no mapping for class " + typeClass.getName ()));
  }

  @Override
  public JavaType typeFromId (DatabindContext context, String typeId) throws IOException {
    return ofNullable (mapping.get (typeId))
      .map (typeClass -> context.getTypeFactory ().constructSpecializedType (baseType, typeClass))
      .orElseThrow (() -> new IllegalStateException ("no mapping for typeId id  " + typeId));
  }

  @Override
  public String getDescForKnownTypeIds () {
    return "register " + classType + " as type id";
  }
}
