import React, {useState} from 'react';
import {Button, Card, Col, List, Row, Tabs} from 'antd';
import FormRegistration from "../component/FormRegistration";
import FormLogin from "../component/FormLogin";
import Item = List.Item;
import {Link} from "react-router-dom";

const RegistrationPage: React.FC = () => {
    const [activeTabKey, setActiveTabKey] = useState<string>('tab1');

    function handleTabChange(key: string) {
        setActiveTabKey(key);
    }

    function handleTabClick(tabKey: string) {
        if (activeTabKey !== tabKey) {
            setActiveTabKey(tabKey);
        }
    }

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

    const pLogUpStyle = {
        fontSize: '20px',
        marginTop: 100,
        marginLeft: 100,
        height: "max-content"
    };

    const pLogInStyle = {
        fontSize: '20px',
        marginTop: 130,
        height: "max-content"
    };

    return (
        <>
            <Tabs
                activeKey={activeTabKey}
                onChange={handleTabChange}
                style={tabStyle}
            >
                <Item key="tab1">
                    <Card hoverable style={cardStyle}>
                        <Row gutter={20}>
                            <Col span={8}>
                                <p style={pLogInStyle}>Пожалуйста, войдите или</p>
                                <Button type={'primary'} onClick={() => handleTabClick('tab2')}>
                                    Создайте аккаунт
                                </Button>
                            </Col>
                            <Col span={13}>
                                <p style={{fontSize: '20px'}}>Авторизация</p>
                                <p>Используйте выш почту или имя ник и пароль для входа</p>
                                <FormLogin/>
                            </Col>

                        </Row>
                    </Card>
                </Item>
                <Item key="tab2">
                    <Card hoverable style={cardStyle}>
                        <Row gutter={20}>
                            <Col span={13}>
                                <p style={{fontSize: '20px'}}>Зарегистрироваться</p>
                                <p>Пожалуйста, предоставьте всю правильную информацию для создания учетной записи</p>
                                <FormRegistration/>
                            </Col>
                            <Col span={8}>
                                <p style={pLogUpStyle}>Создайте аккаунт или</p>
                                <Button style={{marginLeft: 100}} type={'primary'}
                                        onClick={() => handleTabClick('tab1')}>
                                    Войдите
                                </Button>
                            </Col>
                        </Row>
                    </Card>
                </Item>
            </Tabs>
        </>
    );
};

export default RegistrationPage;