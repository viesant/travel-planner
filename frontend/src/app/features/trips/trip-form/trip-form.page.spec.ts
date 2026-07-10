import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TripFormPage } from './trip-form.page';

describe('TripFormPage', () => {
  let component: TripFormPage;
  let fixture: ComponentFixture<TripFormPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TripFormPage],
    }).compileComponents();

    fixture = TestBed.createComponent(TripFormPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
