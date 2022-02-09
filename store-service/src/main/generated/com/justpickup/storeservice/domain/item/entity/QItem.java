package com.justpickup.storeservice.domain.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -2047337460L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final com.justpickup.storeservice.global.entity.QBaseEntity _super = new com.justpickup.storeservice.global.entity.QBaseEntity(this);

    public final com.justpickup.storeservice.domain.category.entity.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.justpickup.storeservice.domain.itemoption.entity.ItemOption, com.justpickup.storeservice.domain.itemoption.entity.QItemOption> itemOptions = this.<com.justpickup.storeservice.domain.itemoption.entity.ItemOption, com.justpickup.storeservice.domain.itemoption.entity.QItemOption>createList("itemOptions", com.justpickup.storeservice.domain.itemoption.entity.ItemOption.class, com.justpickup.storeservice.domain.itemoption.entity.QItemOption.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    public final StringPath name = createString("name");

    public final com.justpickup.storeservice.global.entity.QPhoto photo;

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final EnumPath<com.justpickup.storeservice.global.entity.Yn> salesYn = createEnum("salesYn", com.justpickup.storeservice.global.entity.Yn.class);

    public final com.justpickup.storeservice.domain.store.entity.QStore store;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.justpickup.storeservice.domain.category.entity.QCategory(forProperty("category"), inits.get("category")) : null;
        this.photo = inits.isInitialized("photo") ? new com.justpickup.storeservice.global.entity.QPhoto(forProperty("photo")) : null;
        this.store = inits.isInitialized("store") ? new com.justpickup.storeservice.domain.store.entity.QStore(forProperty("store"), inits.get("store")) : null;
    }

}

