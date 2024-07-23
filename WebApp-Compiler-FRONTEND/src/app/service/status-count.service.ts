import { Injectable } from '@angular/core';
import { StatusCountModel } from '../models/status-count-model';

@Injectable({
  providedIn: 'root'
})
export class StatusCountService {
    statusCountList: StatusCountModel = new StatusCountModel();

  constructor() { }

  public setUserType(statusCountList,data){
    let total = 0;
    // statusCountList.failed = 0;
    for (let index = 0; index < data.length; index++) {
        if (data[index].name == 'STUDENT') {
            statusCountList.student = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'QUESTIONNAIRE') {
            statusCountList.questionnaire = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'ADMIN') {
            statusCountList.admin = data[index].count;
            total = total + data[index].count;
        }
    }
  }

  public setStatusCount(statusCountList, data) {
    let total = 0;

    for (let index = 0; index < data.length; index++) {
        if (data[index].name == 'PROCESSD') {
            statusCountList.processd = data[index].count;
            total = total + data[index].count;

        } else if (data[index].name == 'VERIFIED') {
            statusCountList.verified = data[index].count;
            // this.statusCountList1.verified=data[index].count;
            total = total + data[index].count;

        } else if (data[index].name == 'REJECT') {
            statusCountList.reject = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'SUCCESS') {
            statusCountList.success = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name.toUpperCase() == 'FAILED') {
            statusCountList.failed = statusCountList.failed + data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'REPAIR') {
            statusCountList.repair = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'DELETED') {
            statusCountList.delete = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'APPLIED') {
            statusCountList.applied = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'EXCEPT') {
            statusCountList.except = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'ACCUP') {
            statusCountList.accup = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'ACCEPTED') {
            statusCountList.accepted = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'RETURN') {
            statusCountList.return = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'REJECTED') {
            statusCountList.rejected = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'SENT') {
            statusCountList.sent = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'CANCELLED') {
            statusCountList.cancelled = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'DELETED') {
            statusCountList.deleted = data[index].count;
            total = total + data[index].count;
        } else if (data[index].name == 'ACCEPT') {
            statusCountList.accept = data[index].count;
            total = total + data[index].count;
        // } else if (data[index].name == 'CENCELLED') {
        //     statusCountList.cancelled = data[index].count;
        //     total = total + data[index].count;
        }
        else if (data[index].name == 'STUDENT') {
            statusCountList.active = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'QUESTIONNAIRE') {
            statusCountList.received = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'ADMIN') {
            statusCountList.inactive = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'TO BE CANCEL') {
          statusCountList.toBeCancel = data[index].count;
          total = total + data[index].count;
        }
        else if (data[index].name == 'COMPLETE') {
            statusCountList.complete = data[index].count;
            total = total + data[index].count;
        }
  else if (data[index].name == 'COMPLETE') {
            statusCountList.complete = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'PENDING') {
            statusCountList.pending = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name.toUpperCase() == 'COMPLETED') {
            statusCountList.completed = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'TOBE') {
            statusCountList.tobe = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'DEBITR') {
            statusCountList.debitr = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'AMENDED') {
            statusCountList.amended = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'PRG') {
            statusCountList.prg = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'MCQ') {
            statusCountList.mcq = data[index].count;
            total = total + data[index].count;
        }
        else if (data[index].name == 'PENDING REPROCESS') {
            statusCountList.pendingReprocess = data[index].count;
            total = total + data[index].count;
        }
    }
    statusCountList.totalStatus = total;
  }
}
