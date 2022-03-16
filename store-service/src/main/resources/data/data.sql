-- store
INSERT INTO public.store(
    store_id, created_at, created_by, last_modified_at, last_modified_by, city, street, zipcode, business_end_time, business_start_time, phone_number, photo_name, photo_path, user_id, map_id)
VALUES (1, now(), 1, now(), 1, '서울시', '광화문로', '123-456', to_timestamp('20:00:00', 'HH24:MI:SS'), to_timestamp('09:00:00', 'HH24:MI:SS'), '010-9418-1307', '사진명1', '/Users/sangbum/Desktop', 1, null);


-- category
INSERT INTO public.category(
    category_id, created_at, created_by, last_modified_at, last_modified_by, name, orders, store_id)
VALUES (10, now(), 1, now(), 1, '카테고리1', 0, 1);

INSERT INTO public.category(
    category_id, created_at, created_by, last_modified_at, last_modified_by, name, orders, store_id)
VALUES (11, now(), 1, now(), 1, '카테고리2', 1, 1);

-- item
INSERT INTO public.item(
    item_id, created_at, created_by, last_modified_at, last_modified_by, name, photo_name, photo_path, price, sales_yn, category_id, store_id)
VALUES (100, now(), 1, now(), 1, '아이템1', '아이템_사진명', '/Users/sangbum/Desktop', 1000, 'Y', 10, 1);

INSERT INTO public.item(
    item_id, created_at, created_by, last_modified_at, last_modified_by, name, photo_name, photo_path, price, sales_yn, category_id, store_id)
VALUES (101, now(), 1, now(), 1, '아이템2', '아이템_사진명2', '/Users/sangbum/Desktop', 2000, 'Y', 10, 1);

INSERT INTO public.item(
    item_id, created_at, created_by, last_modified_at, last_modified_by, name, photo_name, photo_path, price, sales_yn, category_id, store_id)
VALUES (102, now(), 1, now(), 1, '아이템3', '아이템_사진명3', '/Users/sangbum/Desktop', 3000, 'Y', 11, 1);


-- item option
INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1000, now(), 1, now(), 1, 'ICE', 'REQUIRED',  100);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1001, now(), 1, now(), 1, 'HOT', 'REQUIRED',  100);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1002, now(), 1, now(), 1, '샷 추가', 'OTHER',  100);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1003, now(), 1, now(), 1, '투샷 추카', 'OTHER',  100);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1004, now(), 1, now(), 1, 'ICE', 'REQUIRED',  101);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1005, now(), 1, now(), 1, 'HOT', 'REQUIRED',  101);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1006, now(), 1, now(), 1, '샷 추가', 'OTHER',  101);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1007, now(), 1, now(), 1, '투샷 추카', 'OTHER',  101);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1008, now(), 1, now(), 1, 'ICE', 'REQUIRED',  102);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1009, now(), 1, now(), 1, 'HOT', 'REQUIRED',  102);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1010, now(), 1, now(), 1, '샷 추가', 'OTHER',  102);

INSERT INTO public.item_option(
    item_option_id, created_at, created_by, last_modified_at, last_modified_by, name, option_type,  item_id)
VALUES (1011, now(), 1, now(), 1, '투샷 추카', 'OTHER',  102);