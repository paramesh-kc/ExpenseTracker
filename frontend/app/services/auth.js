import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class AuthService extends Service {
  @tracked userId = localStorage.getItem('userId');
  @tracked name = localStorage.getItem('name');
  @tracked email = localStorage.getItem('email');
  // NO token stored! Cookie handles authentication.

  get isAuthenticated() {
    return !!this.userId;
  }

  setUser(data) {
    this.userId = data.userId;
    this.name = data.name;
    this.email = data.email;
    // Store ONLY user info (for display), NOT the token
    localStorage.setItem('userId', data.userId);
    localStorage.setItem('name', data.name);
    localStorage.setItem('email', data.email);
  }

  clearUser() {
    this.userId = null;
    this.name = null;
    this.email = null;
    localStorage.removeItem('userId');
    localStorage.removeItem('name');
    localStorage.removeItem('email');
  }

  logout() {
    this.clearUser();
  }
}