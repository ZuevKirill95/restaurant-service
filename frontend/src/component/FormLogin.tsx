import React from 'react';
import {Button, Form, Input, message} from 'antd';
import authService from '../services/authService';
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {Link, useNavigate} from 'react-router-dom';
import {Login} from "../types/types";


const FormLogin: React.FC = () => {
    const navigate = useNavigate();

    function onFinish(values: Login) {
        authService.login(values).then((user) => {
            console.log('Success:', user);
            navigate("/dishes");

        }, (error) => {
            const _content = (error.response && error.response.data)
            console.log(_content);
            message.error("Неправильный логин или пароль");
        })
    }

    return (
        <Form
            name="basic"
            labelCol={{span: 8}}
            wrapperCol={{span: 16}}
            style={{maxWidth: 600}}
            initialValues={{remember: true}}
            onFinish={onFinish}
            autoComplete="off"
        >
            <Form.Item
                label="Логин"
                name="employeeName"
                rules={[{required: true, message: 'Пожалуйста введите вашу почту или ник!'}]}
            >
                <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Ник или почта"/>
            </Form.Item>

            <Form.Item
                label="Пароль"
                name="password"
                rules={[{required: true, message: 'Пожалуйста введите ваш пароль!'}]}
            >
                <Input.Password prefix={<LockOutlined className="site-form-item-icon"/>} placeholder="Пароль"/>
            </Form.Item>

            <Form.Item wrapperCol={{offset: 8, span: 16}}>
                <Button type="primary" htmlType="submit">
                    Войти
                </Button>
                <Link style={{ marginLeft: 20 }}  to="/recovery">Восстановить пароль</Link>
            </Form.Item>
        </Form>
    );
};

export default FormLogin;