import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';

export default class TransactionsController extends Controller {
  @service store;
  @service auth;

  @tracked showForm = false;
  @tracked amount = '';
  @tracked type = 'EXPENSE';
  @tracked note = '';
  @tracked date = '';
  @tracked categoryId = '';
  @tracked errorMessage = '';
  @tracked isSubmitting = false;
  @tracked localTransactions = null;

  get transactionsList() {
    return this.localTransactions || this.model.transactions;
  }

  get sortedTransactions() {
    return this.transactionsList
      .slice()
      .sort((a, b) => new Date(b.date) - new Date(a.date));
  }

  get userId() {
    return this.auth.user?.id || 1;
  }

  @action
  toggleForm() {
    this.showForm = !this.showForm;
    this.errorMessage = '';
  }

  @action
  updateAmount(e) {
    this.amount = e.target.value;
  }

  @action
  updateType(e) {
    this.type = e.target.value;
  }

  @action
  updateNote(e) {
    this.note = e.target.value;
  }

  @action
  updateDate(e) {
    this.date = e.target.value;
  }

  @action
  updateCategoryId(e) {
    this.categoryId = e.target.value;
  }

  @action
  async createTransaction(event) {
    event.preventDefault();
    this.errorMessage = '';
    this.isSubmitting = true;

    try {
      const created = await this.store.ajax(
        `${ENV.APP.API_HOST}/api/transactions?userId=${this.userId}`,
        {
          type: 'POST',
          contentType: 'application/json',
          data: JSON.stringify({
            amount: Number(this.amount),
            type: this.type,
            note: this.note,
            date: this.date,
            categoryId: Number(this.categoryId),
          }),
        }
      );

      this.localTransactions = [...this.transactionsList, created];

      this.amount = '';
      this.note = '';
      this.date = '';
      this.categoryId = '';
      this.showForm = false;
    } catch (err) {
      this.errorMessage = err.payload?.errors?.[0] || err.payload?.error || 'Could not create transaction';
    } finally {
      this.isSubmitting = false;
    }
  }

  @action
  async deleteTransaction(transaction) {
    try {
      await this.store.ajax(
        `${ENV.APP.API_HOST}/api/transactions/${transaction.id}?userId=${this.userId}`,
        { type: 'DELETE' }
      );
      this.localTransactions = this.transactionsList.filter((t) => t.id !== transaction.id);
    } catch (err) {
      this.errorMessage = err.payload?.error || 'Could not delete transaction';
    }
  }
}