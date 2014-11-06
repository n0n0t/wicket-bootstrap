package de.agilecoders.wicket.samples.pages;

import com.google.common.collect.Lists;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * select page
 *
 * @author Alexey Volkov
 * @since 02.11.14
 */
@MountPath(value = "/select")
public class SelectPage extends BasePage {

    private static final long serialVersionUID = -7206242562056511774L;

    private static final List<String> data = Lists.newArrayList(
            "Alabama", "Alaska", "Arizona", "Arkansas",
            "California", "Colorado", "Connecticut",
            "Delaware", "Florida", "Georgia", "Hawaii",
            "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
            "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota",
            "Mississippi", "Missouri", "Montana", "Nebraska",
            "Nevada", "New Hampshire", "New Jersey",
            "New Mexico", "New York", "North Dakota",
            "North Carolina", "Ohio", "Oklahoma", "Oregon",
            "Pennsylvania", "Rhode Island", "South Carolina",
            "South Dakota", "Tennessee", "Texas", "Utah",
            "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"
    );

    private final String formHtmlCode = "Default select\n"
            + "<select wicket:id=\"default\">\n"
            + "</select>\n"
            + "Select with live search\n"
            + "<select wicket:id=\"default-live-search\">\n"
            + "</select>\n"
            + "Default multi select\n"
            + "<select wicket:id=\"multi\">\n"
            + "</select>\n"
            + "Multi select with live search\n"
            + "<select wicket:id=\"multi-live-search\">\n"
            + "</select>\n"
            + "Multi select with limited selection\n"
            + "<select wicket:id=\"multi-max-count-2\">\n"
            + "</select>\n";
    private final String formJavaCode = "add(new BootstrapSelect<String>(\"default\"),\n"
            + "add(new BootstrapSelect<String>(\"default-live-search\").with(\n" +
            "new BootstrapSelectConfig().withLiveSearch(true)));\n"
            + "add(\n"
            + "    new BootstrapMultiSelect<String>(\"multi\"),\n"
            + "    new BootstrapMultiSelect<String>(\"multi-live-search\").with(\n" +
            "new BootstrapSelectConfig().withLiveSearch(true)),\n"
            + "    new BootstrapMultiSelect<String>(\"multi-max-count-2\").with(\n" +
            "new BootstrapSelectConfig().withLiveSearch(true).withMaxOptions(2))\n"
            + ");\n";

    private final String i18nJavaCode = "BootstrapSelectConfig config = new BootstrapSelectConfig();\n"
            + "            config\n"
            + "                    .withNoneSelectedText(\"My nothing selected\")\n"
            + "                    .withNoResultText(\"My no results found\")\n"
            + "                    .withCountSelectedText(\"My selected {0} from {1}\")\n"
            + "                    .withMaxOptionsText(\"My limit ({n} {var} max)\",\n"
            + "                            \"My group limit({n} {var} max)\",\n"
            + "                            \"items\", \"item\");\n";

    /**
     * Construct.
     *
     * @param parameters the current page parameters.
     */
    public SelectPage(PageParameters parameters) {
        super(parameters);
        add(fragment("form", false));
        add(
                new Code("default-html-code", Model.of(formHtmlCode)).setShowLineNumbers(true),
                new Code("default-java-code", Model.of(formJavaCode)).setShowLineNumbers(true));
        add(fragment("form-internationalization", true));
        add(new Code("language-html-code", Model.of(i18nJavaCode)).setShowLineNumbers(true));
        add(fragment("form-modal", false));
    }

    private BootstrapSelect<String> newSelect(String wicketId, BootstrapSelectConfig config) {
        return new BootstrapSelect<String>(wicketId, data).with(config);
    }

    private BootstrapMultiSelect<String> newMultiSelect(String wicketId, BootstrapSelectConfig config) {
        return new BootstrapMultiSelect<String>(wicketId, data).with(config);
    }

    private Fragment fragment(String wicketId, boolean i18n) {
        Fragment fr = new Fragment(wicketId, "typical-form", this);
        final BootstrapSelect<String> first = newSelect("default", of(i18n));
        fr.add(
                first,
                newSelect("default-live-search", of(i18n).withLiveSearch(true)));
        fr.add(
                newMultiSelect("multi", of(i18n)),
                newMultiSelect("multi-live-search", of(i18n).withLiveSearch(true)),
                newMultiSelect("multi-max-count-2", of(i18n).withLiveSearch(true).withMaxOptions(2))
        );
        fr.add(new AjaxLink<Void>("update-default") {

            private static final long serialVersionUID = -3698659776363173730L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                target.add(first);
            }
        });
        return fr;
    }

    private BootstrapSelectConfig of(boolean i18n) {
        BootstrapSelectConfig config = new BootstrapSelectConfig();
        if (i18n) {
            //            "Limit reached ({n} {var} max)",
            //                    "Group limit reached ({n} {var} max)",
            //                    new Object[] { "items", "item" }
            config
                    .withNoneSelectedText("My nothing selected")
                    .withNoResultText("My no results found")
                    .withCountSelectedText("My selected {0} from {1}")
                    .withMaxOptionsText("My limit ({n} {var} max)",
                            "My group limit({n} {var} max)",
                            "items", "item");
        }
        return config;
    }

    @Override
    protected boolean hasNavigation() {
        return true;
    }
}
