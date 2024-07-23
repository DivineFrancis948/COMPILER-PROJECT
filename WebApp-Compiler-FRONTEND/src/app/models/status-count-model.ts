export class StatusCountModel 
{
    delete: number;
    deleted: number;
    processd: number;
    totalStatus: number;
    verified: number;
    reprocess:number;
    student:number;
    admin:number;
    questionnaire:number;
    mcq:number;
    prg:number;
    constructor() 
    {
        this.delete = 0;
        this.deleted = 0;
        this.processd = 0;
        this.totalStatus = 0;
        this.verified = 0;
        this.reprocess=0;
        this.student=0;
        this.admin=0;
        this.questionnaire=0;
        this.mcq = 0;
        this.prg = 0;
    }
}
