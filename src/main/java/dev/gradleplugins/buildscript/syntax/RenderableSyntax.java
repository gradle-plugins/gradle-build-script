package dev.gradleplugins.buildscript.syntax;

import dev.gradleplugins.buildscript.ast.Node;

public final class RenderableSyntax implements Syntax {
    private final Renderer render;

    public RenderableSyntax(Renderer render) {
        this.render = render;
    }

    @Override
    public String render(Node node) {
        return render.render(node).toString();
    }

    interface Renderer {
        Content render(Node node);
    }
}
