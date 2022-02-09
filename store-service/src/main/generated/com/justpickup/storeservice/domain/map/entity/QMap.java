package com.justpickup.storeservice.domain.map.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMap is a Querydsl query type for Map
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMap extends EntityPathBase<Map> {

    private static final long serialVersionUID = -1253033268L;

    public static final QMap map = new QMap("map");

    public final com.justpickup.storeservice.global.entity.QBaseEntity _super = new com.justpickup.storeservice.global.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    public QMap(String variable) {
        super(Map.class, forVariable(variable));
    }

    public QMap(Path<? extends Map> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMap(PathMetadata metadata) {
        super(Map.class, metadata);
    }

}

