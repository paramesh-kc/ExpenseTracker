import Route from '@ember/routing/route';
import { service } from '@ember/service';

export default class DashboardRoute extends Route {
  @service auth;
  @service api;
  @service router;

  beforeModel() {
    if (!this.auth.isAuthenticated) {
      this.router.transitionTo('login');
    }
  }

  async model() {
    const now = new Date();
    const month = now.getMonth() + 1;
    const year = now.getFullYear();
    try {
      return await this.api.getDashboard(month, year);
    } catch (e) {
      return { totalIncome: 0, totalExpense: 0, balance: 0,
        byCategory: [], dailyExpenses: [] };
    }
  }
}