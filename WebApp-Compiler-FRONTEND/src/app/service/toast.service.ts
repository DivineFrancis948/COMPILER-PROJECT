import { Injectable } from '@angular/core';
declare var $: any;

@Injectable({
  providedIn: 'root'
})

export class ToastService
{

  constructor() { }

  savedToast: any = {};
  toastPosition = "";
  toastType = "";
  ShowToasts(title: string, subtitle: string, message: string, type: string) 
        {
            let iconName = ""
            if (type == "Success") {
                this.toastType = "bg-success";
                iconName = "fas fa-info-circle";
            } else if (type == "Info") {
                this.toastType = "bg-info";
                iconName = "fas fa-info-circle";
            } else if (type == "Warning") {
                this.toastType = "bg-warning";
                iconName = "fas fa-exclamation-circle";
            }
            else if (type == "Danger") {
                this.toastType = "bg-danger";
                iconName = "fas fa-exclamation-triangle";
            }
            if (this.savedToast.position != undefined) {
                if (this.savedToast.position == "TL") {
                    this.toastPosition = "topLeft";
                } else if (this.savedToast.position == "TR") {
                    this.toastPosition = "topRight";
                } else if (this.savedToast.position == "BL") {
                    this.toastPosition = "bottomLeft";
                }
                else if (this.savedToast.position == "BR") {
                    this.toastPosition = "bottomRight";
                }
                else if (this.savedToast.position == "TC") {
                    this.toastPosition = "topCenter";
                }
                else if (this.savedToast.position == "BC") {
                    this.toastPosition = "bottomCenter";
                }
            } else {
                this.toastPosition = "topCenter";
            }


            $(document).Toasts('create', {
                class: this.toastType,
                title: message,
                subtitle: '',
                position: this.toastPosition,
                icon: iconName,
                autohide: true,
                delay: 1000,
            })
            return 1;
        }
}
