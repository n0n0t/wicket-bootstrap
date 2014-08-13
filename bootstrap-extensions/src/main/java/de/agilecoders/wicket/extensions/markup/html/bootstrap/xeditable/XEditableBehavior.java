package de.agilecoders.wicket.extensions.markup.html.bootstrap.xeditable;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.xeditable.css.XEditableCssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.xeditable.js.XEditableJsReference;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.StringValue;

import static de.agilecoders.wicket.jquery.JQuery.$;


/**
 * X-Editable for Bootstrap3
 *
 * This library allows you to create editable elements on your page.
 * It includes both popup and inline modes.
 *
 * @link http://vitalets.github.io/x-editable/index.html
 */
public class XEditableBehavior extends Behavior {

    private XEditableOptions options;

    public XEditableBehavior() {
        this(new XEditableOptions());
    }

    public XEditableBehavior(XEditableOptions options) {
        this.options = options;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.render(JavaScriptHeaderItem.forReference(XEditableJsReference.INSTANCE));
        response.render(CssHeaderItem.forReference(XEditableCssReference.INSTANCE));

        response.render($(component).chain("editable", options).asDomReadyScript());
    }

    /**
     * Fired when new value was submitted.
     *
     * @param target the ajax request target
     * @param value  the new value
     */
    protected void onSave(AjaxRequestTarget target, String value) {

    }

    /**
     * Fired when container was hidden. It occurs on both save or cancel.
     *
     * @param target the ajax request target
     * @param reason caused hiding
     */
    protected void onHidden(AjaxRequestTarget target, Reason reason) {

    }

    @Override
    public void bind(Component component) {
        super.bind(component);
        component.add(newSaveListener());
        component.add(newHiddenListener());
    }

    protected AjaxEventBehavior newSaveListener() {
        return new AjaxEventBehavior("save") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                StringValue newValue = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("newValue");
                onSave(target, newValue.toString());
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                attributes.getDynamicExtraParameters().add("return [{'name':'newValue', 'value': attrs.event.extraData.newValue}]");
            }
        };
    }

    protected AjaxEventBehavior newHiddenListener() {
        return new AjaxEventBehavior("hidden") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                StringValue reason = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("reason");
                onHidden(target, Reason.valueOf(reason.toString().toUpperCase()));
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);
                attributes.getDynamicExtraParameters().add("return [{'name':'reason', 'value': attrs.event.extraData}]");
            }
        };
    }

    public enum Reason {
        SAVE, CANCEL, ONBLUR, NOCHANGE, MANUAL
    }

}
