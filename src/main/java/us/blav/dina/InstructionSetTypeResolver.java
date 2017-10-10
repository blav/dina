package us.blav.dina;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionSetTypeResolver implements TypeIdResolver {

  private JavaType baseType;

  @Override
  public JsonTypeInfo.Id getMechanism () {
    return JsonTypeInfo.Id.CUSTOM;
  }

  @Override
  public void init (JavaType baseType) {
    this.baseType = baseType;
  }

  @Override
  public String idFromValue (Object value) {
    return idFromValueAndType (value, value.getClass ());
  }

  @Override
  public String idFromBaseType () {
    return idFromValueAndType (null, baseType.getRawClass ());
  }

  private static final Pattern PACKAGE = Pattern.compile ("^us\\.blav\\.dina\\.is\\.([^\\.]+)$");

  @Override
  public String idFromValueAndType (Object obj, Class<?> clazz) {
    Matcher matcher = PACKAGE.matcher (clazz.getPackage ().getName ());
    if (matcher.matches () == false)
      throw new IllegalStateException (
        "InstructionSetTypeResolver should only be used for " +
          "InstructionSetConfig subclasses named ModuleConfig in us.blav.dina.is.<xx> package");

    return matcher.group (1);
  }

  @Override
  public JavaType typeFromId(DatabindContext context, String type) throws IOException {
    String clazzName = "us.blav.dina.is." + type + ".ModuleConfig";
    try {
      //return TypeFactory.defaultInstance ().constructSpecializedType (baseType, clazz);
      return context.getTypeFactory ().constructSpecializedType (baseType, Class.forName (clazzName));
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException ("cannot find class '" + clazzName + "'");
    }
  }

  @Override
  public String getDescForKnownTypeIds () {
    return "register randomizer config name used as type id";
  }
}
