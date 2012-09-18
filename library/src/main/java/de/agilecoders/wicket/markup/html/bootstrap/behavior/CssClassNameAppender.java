package de.agilecoders.wicket.markup.html.bootstrap.behavior;

import com.google.common.collect.Lists;
import de.agilecoders.wicket.util.CssClassNames;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.Strings;

import java.util.List;

/**
 * A CssClassNameAppender appends the given value, rather than replace it. This is especially useful
 * for adding CSS classes to markup elements.
 * <p/>
 * <pre>
 *     &lt;span class=&quot;className&quot; wicket:id=&quot;foo&quot;&gt;
 * </pre>
 * <p/>
 * can be modified with these CssClassNameAppender:
 * <p/>
 * <pre>
 * link.add(new CssClassNameAppender(&quot;className2&quot;));
 * link.add(new CssClassNameAppender(Arrays.asList(&quot;className2&quot;,&quot;className3&quot;)));
 * </pre>
 * <p/>
 * this will result in the following markup:
 * <p/>
 * <pre>
 *     &lt;span class=&quot;className className2 className3&quot; wicket:id=&quot;foo&quot; &gt;
 * </pre>
 *
 * @author miha
 * @version 1.0
 */
public class CssClassNameAppender extends AttributeAppender {

    /**
     * The name of the html class attribute name.
     */
    protected static final String ATTRIBUTE_NAME = "class";

    /**
     * Creates an AttributeModifier that appends the appendModel's value to the current value of the
     * class attribute, and will add the attribute when it is not there already.
     *
     * @param appendModel the model supplying a single value to append
     */
    public CssClassNameAppender(IModel<String> appendModel) {
        super(ATTRIBUTE_NAME, appendModel, " ");
    }

    /**
     * Constructor.
     * {@link CssClassNameAppender#CssClassNameAppender(org.apache.wicket.model.IModel)}
     *
     * @param appendValue one or more values to append
     */
    public CssClassNameAppender(String... appendValue) {
        this(Lists.newArrayList(appendValue));
    }

    /**
     * Constructor.
     * {@link CssClassNameAppender#CssClassNameAppender(org.apache.wicket.model.IModel)}
     *
     * @param appendValueList a list of values to append
     */
    public CssClassNameAppender(List<String> appendValueList) {
        this(Model.of(CssClassNames.join(appendValueList)));
    }

    /**
     * Constructor.
     * {@link CssClassNameAppender#CssClassNameAppender(org.apache.wicket.model.IModel)}
     *
     * @param cssClassNameProvider a css class name provider
     */
    public CssClassNameAppender(CssClassNameProvider cssClassNameProvider) {
        this(Model.of(cssClassNameProvider.cssClassName()));
    }

    @Override
    protected String newValue(String currentValue, String appendValue) {
        // Short circuit when one of the values is empty: return the other value.
        if (Strings.isEmpty(currentValue)) {
            return appendValue != null ? appendValue : null;
        } else if (Strings.isEmpty(appendValue)) {
            return currentValue != null ? currentValue : null;
        }

        return CssClassNames.parse(currentValue).add(CssClassNames.parse(appendValue)).asString();
    }
}
