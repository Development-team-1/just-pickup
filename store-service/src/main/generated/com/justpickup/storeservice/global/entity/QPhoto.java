package com.justpickup.storeservice.global.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPhoto is a Querydsl query type for Photo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPhoto extends BeanPath<Photo> {

    private static final long serialVersionUID = 329628753L;

    public static final QPhoto photo = new QPhoto("photo");

    public final StringPath name = createString("name");

    public final StringPath path = createString("path");

    public QPhoto(String variable) {
        super(Photo.class, forVariable(variable));
    }

    public QPhoto(Path<? extends Photo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPhoto(PathMetadata metadata) {
        super(Photo.class, metadata);
    }

}

