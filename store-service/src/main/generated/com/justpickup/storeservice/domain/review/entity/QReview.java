package com.justpickup.storeservice.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -573894826L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.justpickup.storeservice.global.entity.QBaseEntity _super = new com.justpickup.storeservice.global.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    //inherited
    public final NumberPath<Long> lastModifiedBy = _super.lastModifiedBy;

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final com.justpickup.storeservice.global.entity.QPhoto photo;

    public final com.justpickup.storeservice.domain.reviewreply.entity.QReviewReply reviewReply;

    public final NumberPath<Integer> starRating = createNumber("starRating", Integer.class);

    public final com.justpickup.storeservice.domain.store.entity.QStore store;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.photo = inits.isInitialized("photo") ? new com.justpickup.storeservice.global.entity.QPhoto(forProperty("photo")) : null;
        this.reviewReply = inits.isInitialized("reviewReply") ? new com.justpickup.storeservice.domain.reviewreply.entity.QReviewReply(forProperty("reviewReply")) : null;
        this.store = inits.isInitialized("store") ? new com.justpickup.storeservice.domain.store.entity.QStore(forProperty("store"), inits.get("store")) : null;
    }

}

