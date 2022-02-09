package com.justpickup.storeservice.domain.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = 358375596L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStore store = new QStore("store");

    public final com.justpickup.storeservice.global.entity.QBaseEntity _super = new com.justpickup.storeservice.global.entity.QBaseEntity(this);

    public final com.justpickup.storeservice.global.entity.QAddress address;

    public final DateTimePath<java.time.LocalDateTime> businessEndTime = createDateTime("businessEndTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> businessStartTime = createDateTime("businessStartTime", java.time.LocalDateTime.class);

    public final ListPath<com.justpickup.storeservice.domain.category.entity.Category, com.justpickup.storeservice.domain.category.entity.QCategory> categories = this.<com.justpickup.storeservice.domain.category.entity.Category, com.justpickup.storeservice.domain.category.entity.QCategory>createList("categories", com.justpickup.storeservice.domain.category.entity.Category.class, com.justpickup.storeservice.domain.category.entity.QCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.justpickup.storeservice.domain.item.entity.Item, com.justpickup.storeservice.domain.item.entity.QItem> items = this.<com.justpickup.storeservice.domain.item.entity.Item, com.justpickup.storeservice.domain.item.entity.QItem>createList("items", com.justpickup.storeservice.domain.item.entity.Item.class, com.justpickup.storeservice.domain.item.entity.QItem.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    public final com.justpickup.storeservice.domain.map.entity.QMap map;

    public final StringPath phoneNumber = createString("phoneNumber");

    public final com.justpickup.storeservice.global.entity.QPhoto photo;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QStore(String variable) {
        this(Store.class, forVariable(variable), INITS);
    }

    public QStore(Path<? extends Store> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStore(PathMetadata metadata, PathInits inits) {
        this(Store.class, metadata, inits);
    }

    public QStore(Class<? extends Store> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.justpickup.storeservice.global.entity.QAddress(forProperty("address")) : null;
        this.map = inits.isInitialized("map") ? new com.justpickup.storeservice.domain.map.entity.QMap(forProperty("map")) : null;
        this.photo = inits.isInitialized("photo") ? new com.justpickup.storeservice.global.entity.QPhoto(forProperty("photo")) : null;
    }

}

