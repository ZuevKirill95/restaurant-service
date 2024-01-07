import React, {FC, useState} from 'react';
import {Button, Card, Form, Input, message} from 'antd';
import {LockOutlined, MailOutlined, UserOutlined} from "@ant-design/icons";
import {RecoveryPassword} from "../types/types";
import authService from "../services/authService";
import orderService from "../services/orderService";
import { Link } from 'react-router-dom';

const RecoveryPasswordPage: FC = () => {
    const [resetPassword, setResetPassword] = useState<boolean>(false);
    const [token, setToken] = useState<boolean>(false);
    const [form] = Form.useForm();

    const handleFormSubmit = async (values: RecoveryPassword) => {
        setResetPassword(true);
        try {
            await authService.resetPassword(values);
            message.success("пароль успешно изменен");
            setToken(true);
        } catch (err) {
            message.error("Пользователя с такой почтой не существует");
        } finally {
            setResetPassword(false);
        }
    };

    const tabStyle = {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: 250,
        minWidth: 300,
        height: '100vh'
    };

    const cardStyle = {
        minWidth: 700,
        minHeight: 450
    };

    const getToken = () => {
        setToken(true);
        try {
            const ff: RecoveryPassword = form.getFieldsValue();
            authService.resetPasswordToken("mish.uxin2012@yandex.ru")
        } catch (err) {
            message.error("Пользователя с такой почтой не существует");
        } finally {
            setToken(false);
        }
    }

    return (
        <div style={tabStyle}>
            <Card hoverable style={cardStyle}>
                <p style={{fontSize: '20px'}}>Восстановление пароля</p>
                <p>Введите вашу почту, на нее придет код для изменения пароля</p>
                <Form
                    style={{width: "80%"}}
                    onFinish={handleFormSubmit}
                    form={form}
                >
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
                        label="Пароль"
                        name="password"
                        rules={[{required: true, message: 'Пожалуйста введите пароль!'}]}
                    >
                        <Input.Password
                            prefix={<LockOutlined className="site-form-item-icon"/>}
                            placeholder="Пароль"
                        />
                    </Form.Item>

                    <Form.Item
                        label="Код"
                        name="token"
                        rules={[{required: true, message: 'Пожалуйста введите ваш код!'}]}
                    >
                        <Input prefix={<MailOutlined className="site-form-item-icon"/>} placeholder="Код"/>

                    </Form.Item>

                    <Button type="primary" onClick={getToken} disabled={token} style={{marginTop: 10, marginLeft: 130, marginBottom: 10}}>Получить код по введенной
                        почте</Button>

                    <Form.Item wrapperCol={{offset: 8, span: 16}}>
                        <Button type="primary" htmlType="submit" disabled={resetPassword}>
                            Заменить пароль
                        </Button>

                    <Link style={{ marginLeft: 20 }}  to="/">Вернуться назад</Link>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
};

export default RecoveryPasswordPage;
