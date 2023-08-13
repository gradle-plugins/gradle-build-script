package dev.gradleplugins.buildscript.blocks;

import dev.gradleplugins.buildscript.ast.statements.BlockStatement;
import dev.gradleplugins.buildscript.ast.statements.Statement;

public final class PluginsDslBlock extends BlockStatement.Builder<PluginsDslBlock> {
    public PluginsDslBlock() {
        super(PluginsDslBlock.class);
    }

    public PluginsDslBlock id(String pluginId) {
        add(IdStatement.id(pluginId));
        return this;
    }

    @Override
    protected PluginsDslBlock newBuilder() {
        return new PluginsDslBlock();
    }

    public static final class IdStatement implements Statement {
        private final String pluginId;

        public IdStatement(String pluginId) {
            this.pluginId = pluginId;
        }

        public static IdStatement id(String pluginId) {
            return new IdStatement(pluginId);
        }

        public String getPluginId() {
            return pluginId;
        }

        @Override
        public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
            return visitor.visit(this);
        }

        //        public static final class Builder {
//            private String pluginId;
//            private String version = null;
//            private Boolean shouldApply = null;
//            private boolean useKotlinAccessor = false;
//
//            private Builder id(String pluginId) {
//                this.pluginId = Objects.requireNonNull(pluginId);
//                return this;
//            }
//
//            public Builder version(String version) {
//                this.version = Objects.requireNonNull(version);
//                return this;
//            }
//
//            public Builder apply(boolean shouldApply) {
//                this.shouldApply = shouldApply;
//                return this;
//            }
//
//            public Builder useKotlinAccessor() {
//                this.useKotlinAccessor = true;
//                return this;
//            }
//
//            public IdStatement build() {
//                final List<Expression> builder = new ArrayList<>();
//                if (useKotlinAccessor) {
//                    final String groovyLiteral = "id '" + pluginId + "'";
//                    final String kotlinLiteral = Arrays.stream(pluginId.split("\\.")).map(it -> {
//                        if (it.contains("-")) {
//                            return "`" + it + "`";
//                        } else {
//                            return it;
//                        }
//                    }).collect(Collectors.joining("."));
//                    builder.add(gradle(groovyLiteral, kotlinLiteral));
//                } else {
//                    builder.add(invoke("id", string(pluginId)));
//                }
//                if (version != null) {
//                    builder.add(Syntax.literal(" "));
//                    builder.add(invoke("version", string(version)));
//                }
//                if (shouldApply != null && !shouldApply) {
//                    builder.add(Syntax.literal(" "));
//                    builder.add(invoke("apply", Syntax.literal("false")));
//                }
//                return new IdStatement(ExpressionStatement.of(concat(builder)));
//            }
//        }
    }
}
