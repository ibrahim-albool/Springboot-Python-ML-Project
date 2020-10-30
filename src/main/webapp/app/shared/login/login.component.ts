import { Component, AfterViewInit, ElementRef, ViewChild, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/core/login/login.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-login-modal',
  templateUrl: './login.component.html',
})
export class LoginModalComponent implements AfterViewInit, OnInit {
  @ViewChild('username', { static: false })
  username?: ElementRef;

  authenticationError = false;

  loginForm = this.fb.group({
    username: [''],
    password: [''],
    rememberMe: [false],
  });

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.isAuthenticated();
  }

  ngAfterViewInit(): void {
    if (this.username) {
      this.username.nativeElement.focus();
    }
  }

  login(): void {
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
        rememberMe: this.loginForm.get('rememberMe')!.value,
      })
      .subscribe(
        () => this.onLoginSuccessful(),
        () => this.onLoginError()
      );
  }
  onLoginSuccessful(): void {
    this.authenticationError = false;
    if (
      this.router.url === '/account/register' ||
      this.router.url.startsWith('/account/activate') ||
      this.router.url.startsWith('/account/reset/')
    ) {
      this.router.navigate(['']);
    }
    this.router.navigate(['']);
  }

  onLoginError(): void {
    this.authenticationError = true;
  }

  register(): void {
    this.router.navigate(['/account/register']);
  }

  requestResetPassword(): void {
    this.router.navigate(['/account/reset', 'request']);
  }

  isAuthenticated(): void {
    if (
      this.accountService.isAuthenticated() === true ||
      sessionStorage.getItem('jhi-authenticationtoken') != null ||
      localStorage.getItem('jhi-authenticationtoken') != null
    ) {
      this.router.navigate(['']);
    }
  }
}
