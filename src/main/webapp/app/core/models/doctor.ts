export class Doctor {
  public position: string;
  public name: string;
  public id: number;
  public description: string;
  public avatar: string;
  public image: string;

  constructor(doctor: Partial<Doctor>) {
    this.position = doctor.position;
    this.name = doctor.name;
    this.id = doctor.id;
    this.avatar = doctor.avatar;
    this.image = doctor.image;
    this.description = doctor.description;
  }
}
