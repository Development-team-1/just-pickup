package com.justpickup.storeservice.domain.favoritestore.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavoriteStore is a Querydsl query type for FavoriteStore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteStore extends EntityPathBase<FavoriteStore> {

    private static final long serialVersionUID = -356764916L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavoriteStore favoriteStore = new QFavoriteStore("favoriteStore");

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

    public final com.justpickup.storeservice.domain.store.entity.QStore store;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QFavoriteStore(String variable) {
        this(FavoriteStore.class, forVariable(variable), INITS);
    }

    public QFavoriteStore(Path<? extends FavoriteStore> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavoriteStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavoriteStore(PathMetadata metadata, PathInits inits) {
        this(FavoriteStore.class, metadata, inits);
    }

    public QFavoriteStore(Class<? extends FavoriteStore> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new com.justpickup.storeservice.domain.store.entity.QStore(forProperty("store"), inits.get("store")) : null;
    }

}

