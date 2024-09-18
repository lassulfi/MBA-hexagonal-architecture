package br.com.fullcycle.application;

public abstract class UseCase<INPUT, OUTPUT> {
    
    // 1. Every use case has an input and an output
    // The use case does not return the entity, the aggregate or the value object

    // 2. The use case implements the command pattern

    public abstract OUTPUT execute(INPUT input);

    public <T> T execute(INPUT input, Presenter<OUTPUT, T> presenter) {
        try {
            return presenter.present(execute(input));
        } catch (Throwable t) {
            return presenter.present(t);
        }
    }
}
