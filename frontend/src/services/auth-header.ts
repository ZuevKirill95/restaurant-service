export default function authHeader() {
    const authDataStore = sessionStorage.getItem('auth-date');
    let authData = null;
    if (authDataStore) {
        authData = JSON.parse(authDataStore);
    }

    if (authData && authData.access_token) {
        return {Authorization: 'Bearer ' + authData.access_token};
    } else {
        return {Authorization: ''};
    }
}