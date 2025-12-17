import {ChangeDetectionStrategy, Component, inject, signal} from '@angular/core';
import {Field, form, required} from '@angular/forms/signals';
import {MatError, MatFormField, MatHint, MatInput, MatInputModule, MatLabel} from "@angular/material/input";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatFormFieldModule} from "@angular/material/form-field";
import * as streamwise from '@streamwise';

@Component({
  selector: 'edit-fixed-flow-modal',
  templateUrl: 'edit-fixed-flow-dialog.component.html',
  styleUrls: ['edit-flow-dialog.component.css'],
  imports: [
    Field,
    MatFormField,
    MatFormFieldModule,
    MatLabel,
    MatInput,
    MatInputModule,
    MatButton,
    MatDatepicker,
    MatDatepickerToggle,
    MatError,
    MatHint,
    MatDatepickerInput,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditFixedFlowDialog {
  dialogRef = inject(MatDialogRef<EditFixedFlowDialog>);
  data: streamwise.EditFixedFlowDialogData = inject(MAT_DIALOG_DATA)

  fixedFlowModel = signal<streamwise.EditFixedFlowDialogData>({
    date: this.data.date,
    amount: this.data.amount,
  });

  flowForm = form(this.fixedFlowModel, (schemaPath) => {
    required(schemaPath.date, {message: 'date is required'});
    required(schemaPath.amount, {message: 'amount is required'});
  });

  onSubmit(event: Event) {
    event.preventDefault()
    this.dialogRef.close(this.fixedFlowModel())
  }
}