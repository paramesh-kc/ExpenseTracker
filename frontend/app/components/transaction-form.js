import Component from '@glimmer/component';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { service } from '@ember/service';
export default class TransactionFormComponent extends Component {
    @service api;
    @service router;
    @tracked amount = '';
    @tracked type = 'EXPENSE';
    @tracked note = '';
    @tracked date = new Date().toISOString().split('T')[0];
    @tracked categoryId = '';
    @tracked error = '';
    @tracked isLoading = false;
    @action updateAmount(e) { this.amount = e.target.value; }
    @action updateType(e) { this.type = e.target.value; }
    @action updateNote(e) { this.note = e.target.value; }
    @action updateDate(e) { this.date = e.target.value; }
    @action updateCategory(e) { this.categoryId = e.target.value; }
    get filteredCategories() {
        return (this.args.categories || []).filter(c => c.type === this.type);
    }
    @action
    async handleSubmit(e) {
        e.preventDefault();
        this.error = '';
        this.isLoading = true;
        try {
            await this.api.createTransaction({
            amount: parseFloat(this.amount),
            type: this.type,
            note: this.note,
            date: this.date,
            categoryId: parseInt(this.categoryId),
            });
            this.router.transitionTo('transactions');
            this.router.refresh();
        } catch (err) {
            this.error = 'Failed to create transaction';
        } finally {
            this.isLoading = false;
        }
    }
}