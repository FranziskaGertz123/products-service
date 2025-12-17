package com.example.products.api.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message<T> {

    private HttpStatus status;
    private T content;

    public Message(T content, HttpStatus status){
        this.content = content;
        this.status = status;
    }
    public static <T> Message<T> of(T target) {
        if (target == null) {
            return of(null, HttpStatus.NOT_FOUND);
        }
        if (target instanceof ApiError) {
            return of(target, HttpStatus.BAD_REQUEST);
        }
        return of(target, HttpStatus.OK);
    }

    public static <T> Message<T> of(T target, HttpStatus status) {
        return new Message<>(target, status);
    }

}