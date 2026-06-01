package com.example.adminltedemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String[][] mappings = {
            { "/dashboard/v2",                              "dashboard/v2" },
            { "/dashboard/v3",                              "dashboard/v3" },
            { "/generate/theme",                            "generate/theme" },

            { "/widgets/small-box",                         "widgets/small-box" },
            { "/widgets/info-box",                          "widgets/info-box" },
            { "/widgets/cards",                             "widgets/cards" },

            { "/layout/unfixed-sidebar",                    "layout/unfixed-sidebar" },
            { "/layout/fixed-sidebar",                      "layout/fixed-sidebar" },
            { "/layout/fixed-header",                       "layout/fixed-header" },
            { "/layout/fixed-footer",                       "layout/fixed-footer" },
            { "/layout/fixed-complete",                     "layout/fixed-complete" },
            { "/layout/layout-custom-area",                 "layout/layout-custom-area" },
            { "/layout/sidebar-mini",                       "layout/sidebar-mini" },
            { "/layout/collapsed-sidebar",                  "layout/collapsed-sidebar" },
            { "/layout/collapsed-sidebar-without-hover",    "layout/collapsed-sidebar-without-hover" },
            { "/layout/logo-switch",                        "layout/logo-switch" },
            { "/layout/layout-rtl",                         "layout/layout-rtl" },

            { "/UI/general",                                "UI/general" },
            { "/UI/icons",                                  "UI/icons" },
            { "/UI/timeline",                               "UI/timeline" },

            { "/mailbox/inbox",                             "mailbox/inbox" },
            { "/mailbox/read",                              "mailbox/read" },
            { "/mailbox/compose",                           "mailbox/compose" },

            { "/forms/elements",                            "forms/elements" },
            { "/forms/layout",                              "forms/layout" },
            { "/forms/validation",                          "forms/validation" },
            { "/forms/wizard",                              "forms/wizard" },

            { "/tables/simple",                             "tables/simple" },
            { "/tables/data",                               "tables/data" },

            { "/pages/profile",                             "pages/profile" },
            { "/pages/settings",                            "pages/settings" },
            { "/pages/invoice",                             "pages/invoice" },
            { "/pages/calendar",                            "pages/calendar" },
            { "/pages/kanban",                              "pages/kanban" },
            { "/pages/chat",                                "pages/chat" },
            { "/pages/file-manager",                        "pages/file-manager" },
            { "/pages/projects",                            "pages/projects" },
            { "/pages/pricing",                             "pages/pricing" },
            { "/pages/faq",                                 "pages/faq" },
            { "/pages/404",                                 "pages/error/404" },
            { "/pages/500",                                 "pages/error/500" },
            { "/pages/maintenance",                         "pages/error/maintenance" },

            { "/examples/login",                            "examples/login" },
            { "/examples/register",                         "examples/register" },
            { "/examples/login-v2",                         "examples/login-v2" },
            { "/examples/register-v2",                      "examples/register-v2" },
            { "/examples/lockscreen",                       "examples/lockscreen" },
        };

        for (String[] m : mappings) {
            registry.addViewController(m[0]).setViewName(m[1]);
        }
    }
}
