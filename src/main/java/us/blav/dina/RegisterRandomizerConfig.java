package us.blav.dina;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import us.blav.dina.randomizers.BitFlipConfig;
import us.blav.dina.randomizers.NopConfig;
import us.blav.dina.randomizers.ShiftConfig;
import us.blav.dina.randomizers.ShuffleConfig;

@JsonIgnoreProperties (ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes ({
  @JsonSubTypes.Type (value = NopConfig.class, name = "nop"),
  @JsonSubTypes.Type (value = ShuffleConfig.class, name = "shuffle"),
  @JsonSubTypes.Type (value = BitFlipConfig.class, name = "bitflip"),
  @JsonSubTypes.Type (value = ShiftConfig.class, name = "shift"),
})
public class RegisterRandomizerConfig {
  public static <CONF extends RegisterRandomizerConfig> RegisterRandomizer<CONF> createRandomizer (VirtualMachine machine, RegisterRandomizer.Factory<?> factory, CONF conf) {
    RegisterRandomizer.Factory<CONF> f = (RegisterRandomizer.Factory<CONF>) factory;
    return f.create (machine, conf);
  }
}
