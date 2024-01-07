import React from "react";
import {Button, Result} from "antd";
import {Link} from "react-router-dom";

export const NotFoundPage: React.FC = () => {
    return (
        <Result
            status="404"
            title="404"
            subTitle="Я тоже не могу понять как такое могло произойти"
            extra={
                <div>
                    <Button type="primary">
                        <Link to="/dishes">Назад на главную</Link>
                    </Button>
                </div>
            }
        />
    );
};

export default NotFoundPage;