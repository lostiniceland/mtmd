CREATE SEQUENCE category_seq INCREMENT BY 1 START WITH 1 NO CYCLE CACHE 50;

create table ice(
    VERSION      BIGINT,
    CREATED      TIMESTAMP,
    UPDATED      TIMESTAMP,
    name varchar NOT NULL,
    category_id     bigint NOT NULL,
    nutrients int NOT NULL,
    purchasePrice varchar,
    retailPrice varchar,
    primary key (name)
);

create table ingredient(
    VERSION      BIGINT,
    CREATED      TIMESTAMP,
    UPDATED      TIMESTAMP,
    name varchar NOT NULL,
    primary key (name)
);

create table ice_ingredients(
    ice_id varchar,
    ingredient_id varchar,
    PRIMARY KEY(ice_id, ingredient_id),
    constraint ice_ingredient foreign key (ice_id) references ice(name),
    constraint ingredient_ice foreign key (ingredient_id) references ingredient(name)
);

create table category (
    id bigint default nextval('category_seq') NOT NULL,
    category_type     varchar NOT NULL,
    primary key (id)
);

create table category_cream (
    id bigint,
    creamInPercent varchar,
    primary key (id),
    constraint cream_category foreign key (id) references category(id)
);

create table category_sorbet (
    id bigint,
    fruitContentInPercent varchar,
    primary key (id),
    constraint sorbet_category foreign key (id) references category(id)
);

create table category_water (
     id bigint,
     primary key (id),
     constraint water_category foreign key (id) references category(id)
);

create table sorbet_fruits (
    sorbet_id bigint,
    fruit varchar,
    constraint sorbet_fruit foreign key (sorbet_id) references category_sorbet(id)
);

create table water_flavour_additives (
    water_id bigint,
    flavour_additive varchar,
    constraint sorbet_fruit foreign key (water_id) references category_water(id)
);
