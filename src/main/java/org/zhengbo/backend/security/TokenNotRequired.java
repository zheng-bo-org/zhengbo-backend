package org.zhengbo.backend.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allow access to the api without token
 * If you need to use this annotation on a restApi method
 * make sure annotated the class with {@link org.springframework.web.bind.annotation.RequestMapping}
 * And on the method only below mapping annotation are allowd.
 * {@link org.springframework.web.bind.annotation.GetMapping}
 * {@link org.springframework.web.bind.annotation.PostMapping}
 * {@link org.springframework.web.bind.annotation.DeleteMapping}
 * {@link org.springframework.web.bind.annotation.PutMapping}
 * {@link org.springframework.web.bind.annotation.PatchMapping}
 *
 * Be careful that path variable in the mapping is not supported for now.
 * This annotation will not work for below pattern :
 * GetMapping("/users/{id}")
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TokenNotRequired {
}
