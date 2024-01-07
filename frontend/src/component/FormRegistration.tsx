import React from 'react';
import {Button, Form, Input} from 'antd';
import authService from '../services/authService';
import {LockOutlined, MailOutlined, PhoneOutlined, UserOutlined} from '@ant-design/icons';
import {useNavigate} from 'react-router-dom';
import {UserRegistration} from "../types/types";

interface Registration {
    employeeName: string;
    email: string;
    password: string;
    branchOffice: string;
}

const FormRegistration: React.FC = () => {
    async function onFinish(values: UserRegistration) {

        authService.register(values).then((massage) => {
            console.log('Success:', massage);
        }, (error) => {
            const _content = (error.response && error.response.data)
            console.log(_content);
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
                label="Ник"
                name="username"
                rules={[{required: true, message: 'Пожалуйста введите ваш ник!'}]}
            >
                <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Ник"/>
            </Form.Item>

            <Form.Item
                label="Имя"
                name="firstName"
                rules={[{required: true, message: 'Пожалуйста введите ваше имя!'}]}
            >
                <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Имя"/>
            </Form.Item>

            <Form.Item
                label="Фамилия"
                name="lastName"
                rules={[{required: true, message: 'Пожалуйста введите вашу фамилию!'}]}
            >
                <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Фамилия"/>
            </Form.Item>

            <Form.Item
                label="Телефон"
                name="phoneNumber"
                rules={[{required: true, message: 'Пожалуйста введите ваш телефон!'}]}
            >
                <Input prefix={<PhoneOutlined className="site-form-item-icon"/>} placeholder="Номер телефона"/>
            </Form.Item>

            <Form.Item
                label="Почта"
                name="email"
                rules={[
                    {type: 'email', message: 'Введите email!'},
                    {required: true, message: 'Пожалуйста введите вашу почту!'},
                ]}
            >
                <Input prefix={<MailOutlined className="site-form-item-icon"/>} placeholder="Почта"/>
            </Form.Item>

            <Form.Item
                label="Id филиала"
                name="idBranchOffice"
                rules={[{required: true, message: 'Пожалуйста введите номер филиала!'}]}
            >
                <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Номер филиала"/>
            </Form.Item>

            <Form.Item
                label="Пароль"
                name="password"
                rules={[{required: true, message: 'Пожалуйста введите пароль!'}]}
            >
                <Input.Password
                    prefix={<LockOutlined className="site-form-item-icon"/>}
                    placeholder="Пароль"
                />
            </Form.Item>

            <Form.Item wrapperCol={{offset: 8, span: 16}}>
                <Button type="primary" htmlType="submit">
                    Зарегистрироваться
                </Button>
            </Form.Item>
        </Form>
    );
};

export default FormRegistration;