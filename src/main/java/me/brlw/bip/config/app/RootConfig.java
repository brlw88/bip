package me.brlw.bip.config.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ww on 28.09.16.
 */
@Configuration
@ComponentScan(basePackages = {"me.brlw.bip.account", "me.brlw.bip.redirection", "me.brlw.bip.statistics", "me.brlw.bip.config"})
public class RootConfig {
}
