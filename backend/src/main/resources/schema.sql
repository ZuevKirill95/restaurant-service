create schema if not exists restaurant_service;

insert into restaurant_service.categories (category) values ('SALAD');
insert into restaurant_service.categories (category) values ('ROLLS');
insert into restaurant_service.categories (category) values ('SECOND_COURSES');
insert into restaurant_service.categories (category) values ('PIZZA');
insert into restaurant_service.categories (category)values ('DRINKS');

insert into restaurant_service.roles (role) values ('SELLER');
insert into restaurant_service.roles (role) values ('ADMIN');

insert into restaurant_service.branchs_office (address, name_city, status) values ('Address office', 'VOLOGDA', 'ON');
insert into restaurant_service.branchs_office (address, name_city, status) values ('Address office2', 'CHEREPOVETS', 'ON');

insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('СВЕЖИЙ ЗЕЛЕНЫЙ САЛАТ', 'Легкий, освежающий зеленый салат с овощами и ароматным дрессингом - здоровый обед.', '80', '500', '1');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ГРЕЧЕСКИЙ САЛАТ', 'Греческий салат с помидорами, огурцом, фетой и ароматными травами - идеальный летний ужин.', '90', '500', '1');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('КАЛИФОРНИЙСКИЙ КИНДЕР-САЛАТ', 'Цветочный салат с фруктами, зеленью и ароматным дрессингом - сочный ужин.', '120', '500', '1');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ИТАЛЬЯНСКИЙ САЛАТ РАДИЧЧО', 'Итальянский салат с радиччо, сыром, миндалем и дрессингом - праздник во рту.', '100', '500', '1');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('АЗИАТСКАЯ НОРИ САЛАТ', 'Азиатский салат с нори, огурцом, кунжутом и пикантным соусом - восточное великолепие.', '110', '500', '1');

insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('СУМАСШЕДШИЙ КРАБОВЫЙ РОЛЛ', 'Ролл с крабом, огурцом и соусом - морское наслаждение в каждом кусочке.', '120', '300', '2');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ТУНЦОВЫЙ ДЕЛЬФИН', 'Тунцевый ролл с авокадо и пикантным соусом - идеальное морское наслаждение.', '130', '300', '2');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ПИКАНТНЫЙ ТОБИКО РОЛЛ', 'Ролл с острым тобико, огурцом и сыром - идеальный выбор пикантного вкуса.', '100', '300', '2');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ЭКЗОТИЧНЫЙ МАНГО РОЛЛ', 'Ролл с манго, пикантным соусом и сыром - уникальное сочетание вкусов.', '110', '300', '2');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('КРЕВЕТОЧНЫЙ КАЛИФОРНИЯ', 'Ролл с креветками, овощами и кунжутом - идеальный выбор морепродуктов.', '120', '300', '2');

insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ШНИЦЕЛЬ ПО-ВЕНСКИ', 'Классический австрийский шницель с картофельным пюре и маринованными овощами.', '180', '250', '3');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('СТЕЙК ИЗ ГОВЯДИНЫ', 'Ароматный говяжий стейк на гриле с жареным картофелем и соусом.', '250', '300', '3');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('КУРИЦА ПО-ФРАНЦУЗСКИ', 'Сочное куриное филе в соусе с шампиньонами, подаётся с рисом или пастой.', '160', '200', '3');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('РЫБНЫЕ КОТЛЕТЫ', 'Нежные рыбные котлеты с пряностями, картофельным пюре и салатом.', '190', '220', '3');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ГАРЛИКОВЫЙ СТЕЙК ИЗ КУРИЦЫ', 'Сочное куриное филе в чесночном соусе с картофельным гарниром и овощами.', '150', '250', '3');

insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ПИЦЦА МАРГАРИТА', 'Классическая пицца с томатным соусом, моцареллой и базиликом.', '120', '200', '4');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ПИЦЦА ПЕПЕРОНИ', 'Горячая пицца с острыми пеперони, моцареллой и томатным соусом.', '130', '220', '4');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ПИЦЦА ГАВАЙСКАЯ', 'Пицца с ананасом, ветчиной и моцареллой на хрустящем тесте.', '140', '240', '4');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ПИЦЦА ВЕГЕТАРИАНСКАЯ', 'Овощи, сыр и тесто - идеально для вегетарианцев.', '135', '230', '4');
insert into restaurant_service.dishes(name, description, price, weight, category_id) VALUES ('ПИЦЦА МЯТНАЯ', 'Пицца с мятой, помидорами и горгонзолой - оригинальное сочетание вкусов.', '150', '250', '4');

insert into restaurant_service.dishes (name, description, price, weight, category_id) VALUES ('Фреш-смузи', 'Сочетание свежих фруктов и охлаждающего льда', '100', '200', '5');
insert into restaurant_service.dishes (name, description, price, weight, category_id) VALUES ('Газированный тоник с лимоном', 'Классический газированный напиток с цитрусовым акцентом', '75', '150', '5');
insert into restaurant_service.dishes (name, description, price, weight, category_id) VALUES ('Имбирный эль', 'Ароматный напиток с нотками имбиря и лимона', '120', '180', '5');
insert into restaurant_service.dishes (name, description, price, weight, category_id) VALUES ('Зеленый чай с мятой', 'Освежающий и тонизирующий напиток', '90', '200', '5');
insert into restaurant_service.dishes (name, description, price, weight, category_id) VALUES ('Фруктовый ледяной чай', 'Сочетание свежих фруктов и ароматного чая', '110', '250', '5');

insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '1');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '2');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '3');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '4');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '5');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '6');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '7');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '8');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '9');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '10');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '11');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '12');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '13');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '14');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '15');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '16');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '17');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '18');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '19');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '20');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '21');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '22');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '23');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '24');
insert into restaurant_service.dishes_branchs_office(branch_office_id, dish_id) VALUES ('1', '25');