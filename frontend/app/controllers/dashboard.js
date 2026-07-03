import Controller from '@ember/controller';
import { action } from '@ember/object';
import { htmlSafe } from '@ember/template';
import { service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class DashboardController extends Controller {
    @service auth;
    @tracked selectedDate = new Date();
    @tracked dashboardData = null;
    @service api;

    @tracked selectedMonth = new Date().getMonth() + 1;
    get monthsList() {
    return [
      { value: 1,  label: 'January' },
      { value: 2,  label: 'February' },
      { value: 3,  label: 'March' },
      { value: 4,  label: 'April' },
      { value: 5,  label: 'May' },
      { value: 6,  label: 'June' },
      { value: 7,  label: 'July' },
      { value: 8,  label: 'August' },
      { value: 9,  label: 'September' },
      { value: 10, label: 'October' },
      { value: 11, label: 'November' },
      { value: 12, label: 'December' },
    ];
    }
    
  get maxDaily() {
    const values = (this.model.dailyExpenses || []).map((d) => d.total);
    return Math.max(...values, 1);
  }

  @action
  barHeight(total) {
    const pct = Math.round((total / this.maxDaily) * 100);
    return htmlSafe(`height: ${pct}%`);
  }
  @action
  testAction(){
    console.log("testAction");
  }

  // this is what the template uses — dashboardData after a filter
  // change, otherwise the initial model from the route
  get currentData() {
    return this.dashboardData || this.model;
  }
  @action
  async onMonthChange(e) {
    try {
      console.log("e.target.value", e.target.value);
      const year = new Date().getFullYear();
      console.log("year", year);
      this.selectedMonth = Number(e.target.value);
      this.dashboardData = await this.api.getDashboard(
        e.target.value,
        year
      );
      console.log("dashboardData", this.dashboardData);
    } catch (e) {
      this.dashboardData = {
        totalIncome: 0, totalExpense: 0, balance: 0,
        byCategory: [], dailyExpenses: [],
      };
    } finally {
      this.isLoading = false;
    }
  }
    // async onDateChange(date) {
    //     this.selectedDate = date;
    //     await this.reloadData();
    // }

    // async reloadData() {
    //   this.isLoading = true;
    //   try {
    //     this.dashboardData = await this.store.ajax(
    //       `http://localhost:8080/api/dashboard/summary?userId=${this.userId}&month=${this.selectedMonth}&year=${this.selectedYear}`,
    //       { type: 'GET' }
    //     );
    //   } finally {
    //     this.isLoading = false;
    //   }
    // }
}