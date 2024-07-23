

export class Programlist {
    Questionid: string;
    QuestionType: string;
    QuestionHeading: string;
    Question: string;

    
  constructor(data: any) {
    this.Questionid = data.Questionid;
    this.QuestionType = data.QuestionType;
    this.QuestionHeading = data.QuestionHeading;
    this.Question = data.Question;
  }

  }