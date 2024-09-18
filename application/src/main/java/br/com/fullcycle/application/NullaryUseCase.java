package br.com.fullcycle.application;

public abstract class NullaryUseCase<OUTPUT> {
    
    // 1. Every use case has an input and an output
    // The use case does not return the entity, the aggregate or the value object

    // 2. The use case implements the command pattern

    public abstract OUTPUT execute();

    public <T> T execute(Presenter<OUTPUT, T> presenter) {
        try {
            return presenter.present(execute());
        } catch (Throwable t) {
            return presenter.present(t);
        }
    }
}
