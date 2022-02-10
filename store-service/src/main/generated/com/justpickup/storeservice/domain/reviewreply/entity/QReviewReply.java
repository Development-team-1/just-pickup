package com.justpickup.storeservice.domain.reviewreply.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewReply is a Querydsl query type for ReviewReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewReply extends EntityPathBase<ReviewReply> {

    private static final long serialVersionUID = -1004430868L;

    public static final QReviewReply reviewReply = new QReviewReply("reviewReply");

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

    public QReviewReply(String variable) {
        super(ReviewReply.class, forVariable(variable));
    }

    public QReviewReply(Path<? extends ReviewReply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewReply(PathMetadata metadata) {
        super(ReviewReply.class, metadata);
    }

}

