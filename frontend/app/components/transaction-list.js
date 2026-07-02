import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class TransactionListComponent extends Component {
  @service api;
  @tracked deletedIds = [];

  get transactions() {
    const txns = this.args.transactions || [];
    return txns.filter(t => !this.deletedIds.includes(t.id));
  }

  @action
  async deleteTransaction(id) {
    if (confirm('Are you sure?')) {
      try {
        await this.api.deleteTransaction(id);
        this.deletedIds = [...this.deletedIds, id];
      } catch (e) {
        alert('Failed to delete');
      }
    }
  }
}