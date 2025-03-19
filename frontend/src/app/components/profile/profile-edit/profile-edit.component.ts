import {Component, EventEmitter, Output} from '@angular/core';
import {NgIf} from '@angular/common';
import {NavigationEnd, Router, RouterLink} from '@angular/router';
import {filter} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {UserInfoResponse} from '../../../models/user/user-info-response.dto';

@Component({
  selector: 'app-profile-edit',
  standalone: true,
  imports: [
    NgIf,
    RouterLink,
    FormsModule,
  ],
  templateUrl: './profile-edit.component.html',
  styleUrl: './profile-edit.component.css'
})
export class ProfileEditComponent {
  @Output() editEnabled = new EventEmitter<boolean>();

  user : UserInfoResponse = {
    userId: 1,
    username: 'ah2122',
    firstName: 'Ahmed',
    lastName: 'Montaser',
    email: 'a@a.com',
    gender: 'Male',
    profilePicture: 'https://placehold.co/128',
    phoneNumber: '+201013598468',
    dateOfBirth: new Date(2025, 2, 5),
    jobTitle: 'Software Developer',
    country: 'Egypt',
    city: 'Giza',
    bio: "Lorem, ipsum dolor sit amet consectetur adipisicing elit. Quisquam debitis labore consectetur voluptatibus mollitia dolorem veniam omnis ut quibusdam minima sapiente repellendus asperiores explicabo, eligendi odit, dolore similique fugiat dolor, doloremque eveniet. Odit, consequatur. Ratione voluptate exercitationem hic eligendi vitae animi nam in, est earum culpa illum aliquam.",
  }

  userDOB : string = `${this.user.dateOfBirth?.getFullYear()}-${this.user.dateOfBirth?.getMonth().toString().padStart(2, '0')}-${this.user.dateOfBirth?.getDate().toString().padStart(2, '0')}`;
  userFullName : string = `${this.user.firstName} ${this.user.lastName}`

  constructor(private router: Router) {}

  ngOnInit() : void {
    // Listen for NavigationEnd events
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        const currentUrl = event.urlAfterRedirects;
        // Update the flag based on route. Adjust your condition as needed.
        if (currentUrl === '/profile/edit') {
          this.editEnabled.emit(true);
        } else {
          this.editEnabled.emit(false);
        }
      });

    console.log(this.userDOB);
  }

  getDOBValue() : string {
    return `${this.user.dateOfBirth?.getFullYear()}-${this.user.dateOfBirth?.getMonth().toString().padStart(2, '0')}-${this.user.dateOfBirth?.getDate().toString().padStart(2, '0')}`;
  }

  parseDOBValue(DOB: string) : void {
    const [year, month, day] = DOB.split('-');
    this.user.dateOfBirth = new Date(Number(year), Number(month), Number(day));
  }
}
