import React from "react";
import user1 from "../georgian_restaurant_employee3.png"
import employee2 from "../georgian_restaurant_employee2.png"
import employee3 from "../georgian_restaurant_employee1.png"
import {Col, Image, Row, Typography} from "antd";

const {Title, Paragraph} = Typography;
export const AboutPage: React.FC = () => {
    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh'}}>
            <div style={{maxWidth: '1300px'}}>
                <Title level={2}>В глубине Грузии</Title>
                <br/>
                <Row align="middle">
                    <Col span={6}>
                        <Image src={user1} alt="user1" style={{maxWidth: 250}}/>
                    </Col>
                    <Col span={12}>
                        <Paragraph style={{fontSize: '18px'}}>
                            Среди зеленых гор и живописных полей жила замечательная семья - семья Шени.
                            В этой семье было все, что нужно для счастья - любовь, единство и страсть к грузинской
                            кухне.
                        </Paragraph>
                        <Paragraph style={{fontSize: '18px'}}>
                            У Шени было трое детей - старший сын Георгий и его близнецы-дочери Мари и Лиа.
                            С самого детства дети проводили время вместе, помогая своей матери в ее приготовлениях.
                            Отец показывал им секреты традиционной грузинской кухни, передавая им свои знания и страсть
                            к
                            готовке.
                        </Paragraph>
                    </Col>
                </Row>
                <br/>
                <Row align="middle">
                    <Col span={14}>
                        <>
                            <Paragraph style={{fontSize: '18px'}}>
                                По мере того как дети росли, стало ясно, что у каждого из них были свои уникальные
                                таланты.
                                Георгий был виртуозным пекарем - он готовил самые нежные и ароматные хачапури, которыми
                                завораживал всех.
                                Мари была мастерицей мясных блюд - каждый ее шашлык был идеально приготовленным и
                                пропитанным ароматами грузинских специй.
                                Лиа, в свою очередь, была экспертом по приготовлению вареников - ее хинкали были просто
                                невероятными, с сочным мясом и нежным тестом.
                            </Paragraph>
                            <Paragraph style={{fontSize: '18px'}}>
                                Однажды, когда дети выросли, они решили, что пора поделиться своими талантами с людьми
                                извне.
                                Они решили открыть семейный грузинский ресторан, чтобы предложить гостям настоящий
                                кулинарный опыт.
                            </Paragraph>
                        </>
                    </Col>
                    <Col span={6}>
                        <Image src={employee2} alt="employee2" style={{maxWidth: 250}}/>
                    </Col>
                </Row>
                <br/>
                <Title level={2}>Ресторан "Солнечная Грузия"</Title>
                <br/>
                <Row align="middle">
                    <Col span={18}>
                        <Paragraph style={{fontSize: '18px'}}>
                            Так появился ресторан "Солнечная Грузия", где каждый из них создал свое собственное меню,
                            продолжая
                            традиции семьи Шени.
                            Гости могли выбирать из разнообразных хачапури, шашлыков и хинкали, наслаждаясь вкусами и
                            ароматами
                            Грузии, которые каждый день создавали Георгий, Мари и Лиа.
                        </Paragraph>
                    </Col>
                </Row>
                <br/>
                <Row align="middle">
                    <Col span={8}>
                        <Image src={employee3} alt="employee3" style={{maxWidth: 250, marginLeft: 100}}/>
                    </Col>
                    <Col span={12}>
                        <Paragraph style={{fontSize: '18px'}}>
                            Ресторан "Солнечной Грузии" стал популярным местом среди гостей, привлекая их своей
                            атмосферой
                            теплоты и семейной гостеприимностью.
                            Гурманы со всего мира приходили, чтобы попробовать блюда, готовимые с любовью трех
                            талантливых
                            братьев и сестер.
                        </Paragraph>
                        <Paragraph style={{fontSize: '18px'}}>
                            Такова история "Солнечной Грузии" - ресторана, где душа, страсть и традиции Грузии сводятся
                            воедино, создавая место, где каждый гость становится частью этой замечательной семьи и
                            наслаждается
                            уникальным гастрономическим опытом.
                        </Paragraph>
                    </Col>
                </Row>
            </div>
        </div>
    );
};

export default AboutPage;