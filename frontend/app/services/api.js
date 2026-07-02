import Service from '@ember/service';
import { service } from '@ember/service';
import config from 'frontend/config/environment';
export default class ApiService extends Service {
    @service auth;
    host = config.APP.API_HOST;

    async request(endpoint, options = {}) {
        const url = `${this.host}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json',
        };
        // Add JWT token if logged in
        if (this.auth.token) {
            headers['Authorization'] = `Bearer ${this.auth.token}`;
        }
        const response = await fetch(url, {
            ...options,
            headers: { ...headers, ...options.headers },
        });

        const data = await response.json();

        if (!response.ok) {
            throw { status: response.status, errors: data };
        }
        return data;
    }
    
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
    
    async getCategories() {
        const userId = this.auth.userId;
        return this.request(`/api/categories?userId=${userId}`);
    }

    async createCategory(data) {
        const userId = this.auth.userId;
        return this.request(`/api/categories?userId=${userId}`, {
        method: 'POST',
        body: JSON.stringify(data),
        })
    };
    async deleteCategory(id) {
        const userId = this.auth.userId;
        return this.request(`/api/categories/${id}?userId=${userId}`, {
        method: 'DELETE',
        });
    }
    
    async getTransactions(startDate, endDate) {
        const userId = this.auth.userId;
        let url = `/api/transactions?userId=${userId}`;
        if (startDate && endDate) {
        url += `&startDate=${startDate}&endDate=${endDate}`;
        }
        return this.request(url);
    }

    async createTransaction(data) {
        const userId = this.auth.userId;
        return this.request(`/api/transactions?userId=${userId}`, {
        method: 'POST',
        body: JSON.stringify(data),
        });
    }

    async updateTransaction(id, data) {
        const userId = this.auth.userId;
        return this.request(`/api/transactions/${id}?userId=${userId}`, {
        method: 'PUT',
        body: JSON.stringify(data),
        });
    }

    async deleteTransaction(id) {
        const userId = this.auth.userId;
        return this.request(`/api/transactions/${id}?userId=${userId}`, {
        method: 'DELETE',
        });
    }
    
    async getDashboard(month, year) {
        const userId = this.auth.userId;
        return this.request(
        `/api/dashboard/summary?userId=${userId}&month=${month}&year=${year}`
        );
    }
}