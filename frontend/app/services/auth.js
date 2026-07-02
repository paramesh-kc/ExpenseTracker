import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';
export default class AuthService extends Service {
    @tracked token = localStorage.getItem('token');
    @tracked name = localStorage.getItem('name');
    @tracked email = localStorage.getItem('email');
    @tracked userId = localStorage.getItem('userId');
    get isAuthenticated() {
        console.log("isAuthenticated called, token:", this.token);
        
        return !!this.token;
    }
    setUser(data) {
        this.token = data.token;
        this.name = data.name;
        this.email = data.email;
        this.userId = data.userId || '1';
        localStorage.setItem('token', data.token);
        localStorage.setItem('name', data.name);
        localStorage.setItem('email', data.email);
        localStorage.setItem('userId', data.userId || '1');
    }
    logout() {
        this.token = null;
        this.name = null;
        this.email = null;
        this.userId = null;
        localStorage.removeItem('token');
        localStorage.removeItem('name');
        localStorage.removeItem('email');
        localStorage.removeItem('userId');
    }
}