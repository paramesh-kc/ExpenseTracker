import Component from '@glimmer/component';
export default class DashboardSummaryComponent extends Component {
    get income() { return this.args.data?.totalIncome || 0; }
    get expense() { return this.args.data?.totalExpense || 0; }
    get balance() { return this.args.data?.balance || 0; }
    get categories() { return this.args.data?.byCategory || []; }
    get daily() { return this.args.data?.dailyExpenses || []; }
    get maxDaily() {
        const vals = this.daily.map(d => d.total);
        return Math.max(...vals, 1);
    }
    
}