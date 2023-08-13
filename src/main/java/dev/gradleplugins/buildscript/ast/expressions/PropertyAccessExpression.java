package dev.gradleplugins.buildscript.ast.expressions;

import dev.gradleplugins.buildscript.ast.ExpressionBuilder;
import dev.gradleplugins.buildscript.ast.type.Type;

import static dev.gradleplugins.buildscript.ast.expressions.CurrentScopeExpression.current;
import static dev.gradleplugins.buildscript.ast.type.UnknownType.unknownType;

public final class PropertyAccessExpression implements Expression {
    private final Type type;
    private final AccessType accessType;
    private final Expression objectExpression;
    private final String propertyName;

    public PropertyAccessExpression(Type type, AccessType accessType, Expression objectExpression, String propertyName) {
        this.type = type;
        this.accessType = accessType;
        this.objectExpression = objectExpression;
        this.propertyName = propertyName;
    }

    public Expression getObjectExpression() {
        return objectExpression;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public <ReturnType> ReturnType accept(Visitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    public enum AccessType {
        // Field prop
        //   - <prop>
        FIELD,

        // Getter/Setter prop
        //   - if kotlin & boolean -> is<prop>
        //   - if kotlin || groovy -> <prop>
        //   - if java & boolean -> is<prop>()/set<prop>()
        //   - if java -> get<prop>()/set<prop>()
        PLAIN,

        // Gradle prop
        //   - if kotlin/groovy -> <prop>.set(...)/<prop>.get()/<prop>.getOrNull()
        //   - if groovy -> <prop> = .../<prop>.get()/<prop>.getOrNull()
        //   - if java -> get<prop>().set(...)/get<prop>().get()/get<prop>().getOrNull()
        GRADLE,

        // Gradle extra
        //   - if kotlin -> extra.get(<prop>)/extra.set(<prop>, <value>)
        //   - if groovy -> <prop>/<prop> = <value>
        //   - if java -> getExtensions().getExtraProperties().get(<prop>)/getExtensions().getExtraProperties().set(<prop>, <value>)
        EXTRA,

        // Gradle extension
        //   - if kotlin -> extensions.get(<prop>)
        //   - if groovy -> <prop>
        //   - if java -> getExtensions().getByName(<prop>)
        EXTENSION,
        UNKNOWN
    }

    public static ExpressionBuilder<PropertyAccessExpression> property(String name) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(unknownType(), AccessType.UNKNOWN, current(), name));
    }

    public static ExpressionBuilder<PropertyAccessExpression> fieldProperty(String name) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(unknownType(), AccessType.FIELD, current(), name));
    }

    public static ExpressionBuilder<PropertyAccessExpression> plainProperty(String name) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(unknownType(), AccessType.PLAIN, current(), name));
    }

    public static ExpressionBuilder<PropertyAccessExpression> gradleProperty(String name) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(unknownType(), AccessType.GRADLE, current(), name));
    }

    public static ExpressionBuilder<PropertyAccessExpression> extraProperty(String name) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(unknownType(), AccessType.EXTRA, current(), name));
    }

    public static ExpressionBuilder<PropertyAccessExpression> extensionProperty(String name) {
        return new ExpressionBuilder<>(new PropertyAccessExpression(unknownType(), AccessType.EXTENSION, current(), name));
    }
}
