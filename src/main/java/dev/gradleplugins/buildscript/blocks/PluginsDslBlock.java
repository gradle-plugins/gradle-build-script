package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.GradleBlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public final class PluginsDslBlock extends GradleBlockStatement.BlockBuilder<PluginsDslBlock> {
    public PluginsDslBlock() {
        super(PluginsDslBlock.class);
    }

    public PluginsDslBlock id(String pluginId) {
        add(IdStatement.id(pluginId));
        return this;
    }

    public PluginsDslBlock id(String pluginId, String version) {
        add(new IdStatement(pluginId, version, IdStatement.UseKotlinAccessor.NO, IdStatement.ShouldApply.TRUE));
        return this;
    }

    public PluginsDslBlock id(String pluginId, Consumer<? super IdStatement.Builder> action) {
        final IdStatement.Builder builder = new IdStatement.Builder(pluginId);
        action.accept(builder);
        add(builder.build());
        return this;
    }

    @Override
    protected PluginsDslBlock newBuilder() {
        return new PluginsDslBlock();
    }

    public static final class IdStatement implements Statement {
        private final String pluginId;
        @Nullable private final String version;
        private final UseKotlinAccessor useKotlinAccessor;
        private final ShouldApply shouldApply;

        public IdStatement(String pluginId, @Nullable String version, UseKotlinAccessor useKotlinAccessor, ShouldApply shouldApply) {
            this.pluginId = pluginId;
            this.version = version;
            this.useKotlinAccessor = useKotlinAccessor;
            this.shouldApply = shouldApply;
        }

        public static IdStatement id(String pluginId) {
            return new IdStatement(pluginId, null, UseKotlinAccessor.NO, ShouldApply.TRUE);
        }

        public String getPluginId() {
            return pluginId;
        }

        @Nullable
        public String getVersion() {
            return version;
        }

        public boolean shouldUseKotlinAccessor() {
            return useKotlinAccessor == UseKotlinAccessor.YES;
        }

        public boolean shouldApply() {
            return shouldApply == ShouldApply.TRUE;
        }

        @Override
        public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
            return visitor.visit(this);
        }

        private enum UseKotlinAccessor { YES, NO }
        private enum ShouldApply { TRUE, FALSE }

        public static final class Builder {
            private final String pluginId;
            private String version = null;
            private ShouldApply shouldApply = ShouldApply.TRUE;
            private UseKotlinAccessor useKotlinAccessor = UseKotlinAccessor.NO;

            private Builder(String pluginId) {
                this.pluginId = Objects.requireNonNull(pluginId);
            }

            public Builder version(String version) {
                this.version = Objects.requireNonNull(version);
                return this;
            }

            public Builder apply(boolean shouldApply) {
                this.shouldApply = shouldApply ? ShouldApply.TRUE : ShouldApply.FALSE;
                return this;
            }

            public Builder useKotlinAccessor() {
                this.useKotlinAccessor = UseKotlinAccessor.YES;
                return this;
            }

            public IdStatement build() {
                return new IdStatement(pluginId, version, useKotlinAccessor, shouldApply);
            }
        }
    }
}
