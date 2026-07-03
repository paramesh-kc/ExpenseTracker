import Service from '@ember/service';
import { service } from '@ember/service';
import config from 'frontend/config/environment';

export default class ApiService extends Service {
  @service auth;

  host = config.APP.API_HOST;

  // Core fetch — cookie is sent AUTOMATICALLY by browser!
  async request(endpoint, options = {}) {
    const url = `${this.host}${endpoint}`;

    const response = await fetch(url, {
      ...options,
      credentials: 'include',     // THIS sends cookies automatically!
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    // Handle 401/403 — redirect to login
    if (response.status === 401 || response.status === 403) {
      this.auth.clearUser();
      window.location.href = '/login';
      throw { status: response.status, errors: { error: 'Not authenticated' } };
    }

    const data = await response.json();

    if (!response.ok) {
      throw { status: response.status, errors: data };
    }

    return data;
  }

  // AUTH
  async register(name, email, password) {
    return this.request('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify({ name, email, password }),
    });
  }

  async login(email, password) {
    return this.request('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
  }

  async logout() {
    return this.request('/api/auth/logout', { method: 'POST' });
  }

  // CATEGORIES (no userId, no token — cookie handles everything!)
  async getCategories() {
    return this.request('/api/categories');
  }

  async createCategory(data) {
    return this.request('/api/categories', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async deleteCategory(id) {
    return this.request(`/api/categories/${id}`, { method: 'DELETE' });
  }

  // TRANSACTIONS
  async getTransactions(startDate, endDate) {
    let url = '/api/transactions';
    if (startDate && endDate) {
      url += `?startDate=${startDate}&endDate=${endDate}`;
    }
    return this.request(url);
  }

  async createTransaction(data) {
    return this.request('/api/transactions', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  }

  async updateTransaction(id, data) {
    return this.request(`/api/transactions/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  async deleteTransaction(id) {
    return this.request(`/api/transactions/${id}`, { method: 'DELETE' });
  }

  // DASHBOARD
  async getDashboard(month, year) {
    return this.request(`/api/dashboard/summary?month=${month}&year=${year}`);
  }
}