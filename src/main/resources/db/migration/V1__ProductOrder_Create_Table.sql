create table Product_Order (
    id uuid not null,
    description varchar(255) not null not null,
    created_by varchar(100) not null default '99999',
    created_on timestamp(6),
    modified_by varchar(100),
    modified_on timestamp(6),
    status varchar(20) not null default 'DRAFT',
    primary key (id)
);

create table Order_Item (
    id uuid not null,
    product_id uuid not null,
    product_order_id uuid not null,
    quantity integer not null,
    created_by varchar(100) not null default '99999',
    created_on timestamp(6),
    modified_by varchar(100),
    modified_on timestamp(6),
    primary key (id),
    foreign key (product_order_id)  references Product_Order
);


