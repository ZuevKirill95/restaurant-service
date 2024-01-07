import axios from "axios";
import {AuthData, Dish, Login, RecoveryPassword, Registration, User, UserRegistration} from "../types/types";
import {Dispatch} from "redux";
import authHeader from "./auth-header";
import {message} from "antd";
import {updateOrders} from "../slices/orderSlice";

const API_URL = "/api/auth"

function saveLocalStore(authData: AuthData) {
    if (authData.access_token) {
        sessionStorage.setItem('auth-date', JSON.stringify(authData));
    }
}

function saveLocalStoreUser(user: User) {
    if (user.username) {
        sessionStorage.setItem('user', JSON.stringify(user));
    }
}


async function register(registration: UserRegistration) {
    const {username, email, phoneNumber, password, idBranchOffice, firstName, lastName} = registration;
    const response = await axios.post<UserRegistration>(`${API_URL}/signup`, {
        username,
        email,
        phoneNumber,
        password,
        idBranchOffice,
        firstName,
        lastName
    });
    return response.data;
}

async function login(login: Login): Promise<User> {
    const {employeeName, password} = login;

    const responseAuthData = await axios
        .post<AuthData>(`${API_URL}/signin`, {
            username: employeeName,
            password,
        });
    saveLocalStore(responseAuthData.data);


    const headers = authHeader();
    const responseUser = await axios.get<User>(API_URL, {headers});
    saveLocalStoreUser(responseUser.data);

    return responseUser.data;
}

async function resetPasswordToken(email: string) {
    try {
        await axios.post(`${API_URL}/reset-password/token`, {
            email: email
        }).then(message.success(`Письмо отправили на почту: ${email}!`))
    } catch (error) {
        message.error("Неизвестная ошибка при отправлении письма!");
    }
}

async function resetPassword(value: RecoveryPassword) {
    console.log(value);
    return await axios.put(`${API_URL}/reset-password`, {
        email: value.email,
        password: value.password,
        token: value.token,
        });
}

async function refresh(refresh_token: string, dispatch: Dispatch): Promise<User> {
    const responseAuthData = await axios
        .post<AuthData>(`${API_URL}/refresh`, {refresh_token: refresh_token});
    saveLocalStore(responseAuthData.data);


    const headers = authHeader();
    const responseUser = await axios.get<User>(API_URL, {headers});
    saveLocalStoreUser(responseUser.data);

    return responseUser.data;
}

function logout() {
    const headers = authHeader();

    if (headers.Authorization !== ''){
        try {
            sessionStorage.removeItem('auth-date');
            sessionStorage.removeItem('user');
            return axios.put(`${API_URL}/logout`, {},{headers});

        } catch (error) {
            console.error("Ошибка выхода:", error);
            throw error;
        }
    }
}

const authService = {
    register,
    login,
    logout,
    refresh,
    resetPasswordToken,
    resetPassword
};

export default authService;