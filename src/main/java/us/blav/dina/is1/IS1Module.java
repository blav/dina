package us.blav.dina.is1;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import us.blav.dina.*;

import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.name.Names.named;

public class IS1Module extends AbstractModule {

  public static final String MODULE_NAME = "is1";

  @Override
  protected void configure () {
    bind (InstructionProcessor.class)
      .annotatedWith (named (MODULE_NAME))
      .to (IS1Processor.class);

    bind (FaultHandler.class)
      .annotatedWith (named (MODULE_NAME))
      .toInstance ((fault, state) -> {
        System.out.println ("fault");
      });

    Multibinder<InstructionFactory> m = newSetBinder (binder (), InstructionFactory.class, Names.named (MODULE_NAME));
    m.addBinding ().to (Add.class);
    m.addBinding ().to (Alloc.class);
    m.addBinding ().to (Decrement.class);
    m.addBinding ().to (FindBackward.class);
    m.addBinding ().to (FindForward.class);
    m.addBinding ().to (Fork.class);
    m.addBinding ().to (GotoBackward.class);
    m.addBinding ().to (GotoForward.class);
    m.addBinding ().to (IfNotNull.class);
    m.addBinding ().to (IfNull.class);
    m.addBinding ().to (Increment.class);
    m.addBinding ().to (Label.class);
    m.addBinding ().to (Read.class);
    m.addBinding ().to (Substract.class);
    m.addBinding ().to (Write.class);
  }
}
