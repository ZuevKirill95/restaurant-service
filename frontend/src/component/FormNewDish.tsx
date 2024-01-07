import React, {useState} from 'react';
import {Button, Form, Input, message, Select} from 'antd';
import {EditOutlined} from '@ant-design/icons';
import {Category, Dish} from "../types/types";
import dishService from "../services/dishService";
import {Dispatch} from "redux";
import {changeCategory} from "../pages/DishesPage";

interface AddNewDish {
    dispatch: Dispatch;
    category: Category[];
}

const {Option} = Select;

const layout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16},
};

const tailLayout = {
    wrapperCol: {offset: 8, span: 16},
};

const FormNewDish: React.FC<AddNewDish> = ({dispatch, category}) => {
    const [form] = Form.useForm();
    const [categoryFilter, setCategoryFilter] = useState<number>(1);
    const [loading, setLoading] = useState(false);

    function onFinish(values: Dish) {
        dishService.addDish(values, dispatch).then((user) => {
            console.log('Success:', user);

        }, (error) => {
            const _content = (error.response && error.response.data)
            console.log(_content);
            message.error("Недостаточно прав!");
        })
    }

    const onReset = () => {
        form.resetFields();
    };

    const onFill = () => {
        form.setFieldsValue({
            name: 'Pizza with ham',
            urlImage: 'https://www.shutterstock.com/shutterstock/photos/666662188/display_1500/stock-photo-closeup-hand-of-chef-baker-in-white-uniform-making-pizza-at-kitchen-666662188.jpg',
            category: 4,
            description: 'Some text...',
            price: 100,
            weight: 100
        });
    };

    function handleCategoryFilter(value: string) {
        setCategoryFilter(Number.parseInt(value));
    }

    return (
        <Form
            name="basic"
            labelCol={{span: 8}}
            wrapperCol={{span: 16}}
            style={{maxWidth: 600}}
            onFinish={onFinish}
            autoComplete="off"
            form={form}
        >
            <Form.Item
                label="Название"
                name="name"
                rules={[{required: true, message: 'Please input name!'}]}
            >
                <Input prefix={<EditOutlined className="site-form-item-icon"/>} placeholder="Name"/>
            </Form.Item>

            <Form.Item
                label="Url image"
                name="urlImage"
            >
                <Input prefix={<EditOutlined className="site-form-item-icon"/>} placeholder="https://..."/>
            </Form.Item>

            <Form.Item
                label="Категория"
                name="category"
                rules={[{required: true}]}>
                <Select style={{width: 200}} onChange={handleCategoryFilter}>
                    {category.map((item) => (
                        <Select.Option key={item.id} value={item.id}>
                            {changeCategory(item.category)}
                        </Select.Option>
                    ))}
                </Select>
            </Form.Item>

            <Form.Item
                label="Описание"
                name="description"
                rules={[{required: true, message: 'Please input description!'}]}
            >
                <Input prefix={<EditOutlined className="site-form-item-icon"/>} placeholder="Tell me about the dish"/>
            </Form.Item>

            <Form.Item
                label="Цена"
                name="price"
                rules={[{required: true, message: 'Please input price!'}]}
            >
                <Input type="number" prefix={<EditOutlined className="site-form-item-icon"/>} placeholder="100"/>
            </Form.Item>

            <Form.Item
                label="Вес"
                name="weight"
                rules={[{required: true, message: 'Please input weight!'}]}
            >
                <Input type="number" prefix={<EditOutlined className="site-form-item-icon"/>} placeholder="100"/>
            </Form.Item>
            <Form.Item {...tailLayout}>
                <Button type="primary" htmlType="submit" loading={loading} style={{marginLeft: -100, marginRight: 10}}>
                    Добавить
                </Button>
                <Button htmlType="button" onClick={onReset}>
                    Сбросить форму
                </Button>
                <Button type="link" htmlType="button" onClick={onFill}>
                    Заполнить форму
                </Button>
            </Form.Item>
        </Form>
    );
};

export default FormNewDish;