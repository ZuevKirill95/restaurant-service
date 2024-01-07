import React, {useEffect, useState} from 'react';
import {Badge, Button, Collapse, message, Tabs, Tag} from 'antd';
import orderService from "../services/orderService";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../store";
import {DishesOrder, Order, OrderCooked, OrderCooking, OrderReview} from "../types/types";
import ModalCancelOrder from "../component/ModalCancelOrder";

const {Panel} = Collapse;

const OrderPage: React.FC = () => {
    const dispatch = useDispatch();
    const allOrders = useSelector((store: RootState) => store.orders.allOrders);
    const userStr = sessionStorage.getItem('auth-date');
    const [isUpdate, setIsUpdate] = useState(false);
    const currentDate = new Date();

    function onAccept(id: number) {
        console.log(id);
        if (userStr) {
            orderService.updateOrderStatusById(id, 'COOKING', dispatch).then(() => {
                console.log('Success: Accept');
                setIsUpdate(true);
            }, (error) => {
                const _content = (error.response && error.response.data)
                console.log(_content);
                message.error("id не найден");
            })
        }
    }

    function onCooked(id: number, branchId: number, branchAddress: string) {
        orderService.updateOrderStatusById(id, 'COOKED', dispatch).then(() => {
            console.log('Success: Cooked');
            setIsUpdate(true);
        }, (error) => {
            const _content = (error.response && error.response.data)
            console.log(_content);
            message.error("id не найден");
        })
    }

    useEffect(() => {
        const timer = setInterval(() => {
            console.log("Прошла 1 минута!");
            orderService.getListOrdersByNotify(dispatch).then((length) => {
                if (length > 0) message.warning('Обновлено заказов: ' + length);
            }, () => {
                message.error("Пользователь не допущен к работе!");
            });
        }, 30000);

        return () => {
            clearInterval(timer);
        };
    }, [dispatch]);

    useEffect(() => {
        orderService.getListOrders(dispatch).then((data) => {
            console.log('Getting:', data);

        }, () => {
            message.error("Пользователь не допущен к работе!");
        });
        setIsUpdate(false);

    }, [dispatch, isUpdate]);

    const itemsReview = allOrders.filter((item: Order) => item.status === 'REVIEW')
        .map((item: Order) => {
            return {...item, color: 'lightblue', tag_label: 'new', tag_color: 'red'}
        })
        .map((order: OrderReview) => (
            {
                key: order.id + 0.01,
                color_b: order.color,
                label: (
                    <>
                        <p><b>Адрес: </b>{order.address} </p>
                        <p><b>Время от
                            заказывания: </b>{new Date(Date.parse(order.orderTime)).toLocaleString('RU')} минут(ы)</p>
                        <p><b>Ожидание
                            заказа: </b>{Math.floor((currentDate.getTime() - new Date(order.orderTime).getTime()) / 60000)} минут(ы)
                        </p>
                        <Tag color={order.tag_color}>{order.tag_label}</Tag>
                    </>
                ),
                items: (
                    <>
                        <p><b>Имя клиента: </b> {order.clientName}</p>
                        <p><b>Телефон клиента: </b> {order.clientPhone}</p>
                        <p><b>Описание: </b> {order.description}</p>
                        <Collapse accordion>
                            <Panel key={order.id + 0.1} header={<b>Список блюд</b>} showArrow={false}>
                                {order.dishesOrders.map((dish: DishesOrder) => (
                                    <p><b>Имя блюда: </b> {dish.dishName}, <b>Количество: </b> {dish.quantity}</p>))}
                            </Panel>
                        </Collapse>
                        <Button type="primary" onClick={() => onAccept(order.id)}
                                style={{marginRight: 10, marginTop: 10}}>Принять</Button>
                        <ModalCancelOrder id={order.id}/>
                    </>
                ),
            }
        ));

    const itemsCooking = allOrders.filter((item: Order) => item.status === 'COOKING')
        .map((item: any) => {
            return {...item, color: 'lightgreen', tag_label: 'new', tag_color: 'red'}
        })
        .map((order: OrderCooking) => (
            {
                key: order.id + 0.02,
                color_b: order.color,
                label: (
                    <div>
                        <p><b>Адрес: </b>{order.address}</p>
                        <p><b>Время от
                            заказывания: </b>{new Date(Date.parse(order.orderCookingTime)).toLocaleString('RU')} минут(ы)
                        </p>
                        <p>
                            <b>Время от
                                приготовления: </b>{Math.floor((currentDate.getTime() - new Date(order.orderCookingTime).getTime()) / 60000)} минут(ы)
                        </p>
                        <Tag color={order.tag_color}>{order.tag_label}</Tag>
                    </div>
                ),
                items: (
                    <div>
                        <p><b>Имя клиента:</b> {order.clientName}</p>
                        <p><b>Телефон клиента:</b> {order.clientPhone}</p>
                        <p><b>Описание:</b> {order.description}</p>
                        <Collapse accordion>
                            <Panel key={order.id} header={<b>Список блюд</b>} showArrow={false}>
                                {order.dishesOrders.map((dish: DishesOrder) => (
                                    <p><b>Имя блюда: </b> {dish.dishName}, <b>Количество: </b> {dish.quantity}</p>))}
                            </Panel>
                        </Collapse>
                        <Button type="primary" onClick={() => onCooked(order.id, order.branchId, order.branchAddress)}
                                style={{marginTop: 10}}>Завершить</Button>
                    </div>
                ),
            }
        ));

    const itemsCooked = allOrders.filter((item: Order) => item.status === 'COOKED')
        .map((item: any) => {
            return {...item, color: 'lightgrey', tag_label: 'new', tag_color: 'red'}
        })
        .map((order: OrderCooked) => (
            {
                key: order.id + 0.03,
                color_b: order.color,
                label: (
                    <div>
                        <p><b>Адрес: </b>{order.address}</p>
                        <p><b>Время от
                            заказывания: </b>{new Date(Date.parse(order.orderCookedTime)).toLocaleString('RU')} минут(ы)
                        </p>
                        <p>
                            <b>Время ожидания
                                курьера: </b>{Math.floor((currentDate.getTime() - new Date(order.orderCookedTime).getTime()) / 60000)} минут(ы)
                        </p>
                    </div>
                ),
                items: (
                    <div>
                        <p><b>Имя клиента: </b> {order.clientName}</p>
                        <p><b>Телефон клиента: </b> {order.clientPhone}</p>
                        <p><b>Описание: </b> {order.description}</p>
                        <Collapse accordion>
                            <Panel key={order.id} header={<b>Список блюд</b>} showArrow={false}>
                                {order.dishesOrders.map((dish: DishesOrder) => (
                                    <p><b>Имя блюда: </b> {dish.dishName}, <b>Количество: </b> {dish.quantity}</p>))}
                            </Panel>
                        </Collapse>
                    </div>
                ),
            }
        ));

    const itemsAll = [...itemsReview, ...itemsCooking, ...itemsCooked];

    const countReview: number = itemsReview.length;
    const countCooking: number = itemsCooking.length;
    const countCooked: number = itemsCooked.length;
    const countAll: number = itemsAll.length;

    return (
        <div style={{overflow: 'auto'}}>
            <Tabs style={{marginBottom: 24, marginTop: 20,}} tabPosition={'left'}>
                <Tabs.TabPane tab={<Badge count={countReview} offset={[10, 0]}>Заявки</Badge>} key={'1'}>
                    <Collapse accordion>
                        {itemsReview.map((item: any) => (
                            <Panel key={item.key}
                                   header={item.label}
                                   showArrow={false}
                                   style={{backgroundColor: item.color_b, marginBottom: 15}}
                                   id={item.key}>
                                {item.items}
                            </Panel>
                        ))}
                    </Collapse>
                </Tabs.TabPane>

                <Tabs.TabPane tab={<Badge count={countCooking} offset={[10, 0]}>Приготовление</Badge>} key={'2'}>
                    <Collapse accordion>
                        {itemsCooking.map((item: any) => (
                            <Panel key={item.key}
                                   header={item.label}
                                   showArrow={false}
                                   style={{backgroundColor: item.color_b, marginBottom: 15}}
                            >
                                {item.items}
                            </Panel>
                        ))}
                    </Collapse>
                </Tabs.TabPane>

                <Tabs.TabPane tab={<Badge count={countCooked} offset={[10, 0]}>Готово</Badge>} key={'3'}>
                    <Collapse accordion>
                        {itemsCooked.map((item: any) => (
                            <Panel key={item.key}
                                   header={item.label}
                                   showArrow={false}
                                   style={{backgroundColor: item.color_b, marginBottom: 15}}>
                                {item.items}
                            </Panel>
                        ))}
                    </Collapse>
                </Tabs.TabPane>

                <Tabs.TabPane tab={<Badge count={countAll} offset={[10, 0]}>Все заказы</Badge>} key={'4'}>
                    <Collapse accordion>
                        {itemsAll.map((item: any) => (
                            <Panel key={item.key}
                                   header={item.label}
                                   showArrow={false}
                                   style={{backgroundColor: item.color_b, marginBottom: 15}}>
                                {item.items}
                            </Panel>
                        ))}
                    </Collapse>
                </Tabs.TabPane>

            </Tabs>
        </div>
    );
};

export default OrderPage;