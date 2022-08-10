package com.themonkeynauts.game.common.annotation;

import net.devh.boot.grpc.server.service.GrpcService;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@GrpcService
public @interface PresentationAdapter {
}
